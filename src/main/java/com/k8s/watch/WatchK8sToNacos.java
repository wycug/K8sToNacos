package com.k8s.watch;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import com.alibaba.nacos.client.naming.beat.BeatInfo;
import com.alibaba.nacos.client.naming.cache.ServiceInfoHolder;
import com.alibaba.nacos.client.naming.core.ServerListManager;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientManager;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientProxy;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.common.http.DefaultHttpClientFactory;
import com.alibaba.nacos.common.http.HttpClientFactory;
import com.alibaba.nacos.common.http.client.InterceptingHttpClientRequest;
import com.alibaba.nacos.common.http.client.NacosRestTemplate;
import com.alibaba.nacos.common.http.client.request.DefaultHttpClientRequest;
import com.alibaba.nacos.common.http.client.request.HttpClientRequest;
import com.alibaba.nacos.common.http.client.response.HttpClientResponse;
import com.alibaba.nacos.common.model.RequestHttpEntity;
import com.k8s.K8s.K8sTemplate;
import com.k8s.nacos.NacosTemplate;
import com.k8s.nacos.bean.NameSpcaesBean;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Watch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * @auther wy
 * @create 2022/5/14 21:14
 */

@Slf4j
public class WatchK8sToNacos {

    private Map<String, V1Pod> k8sPodMap;

    private Map<String, String> nameSpaces;

    private Map<String, Set<String>> services;


    private final NacosTemplate nacosTemplate;
    private final K8sTemplate k8sTemplate;



    @Resource
    private RestTemplate restTemplate;

    public WatchK8sToNacos(){
        services = new HashMap<>();
        k8sPodMap = new HashMap<>();
        nameSpaces = new HashMap<>();

        nacosTemplate = new NacosTemplate();
        k8sTemplate = new K8sTemplate();

    }

    public void watch() throws ApiException, IOException {

        try (Watch<V1Endpoints> watchAllPod = k8sTemplate.watchAllEndponts()) {
            for (Watch.Response<V1Endpoints> item : watchAllPod) {

                if (item.object!=null&&item.object.getSubsets()!=null&&item.object.getSubsets().size()>0){
                    log.info(item.type, item.object.getMetadata().getName());
                    switch (item.type){
                        case "ADDED":
                            createInstance(item.object);
                            break;
                        case "MODIFIED":
                            break;
                        case "DELETEED":
                            break;
                    }
                }
            }
        }
    }

    public void createInstance(V1Endpoints object){
        String serviceName = object.getMetadata().getName();
        String clusterName = object.getMetadata().getClusterName();
        String namespace = object.getMetadata().getNamespace();

        //判断namespace是否存在，不存在则创建
        if (!nameSpaces.containsKey(namespace)){
            Boolean flag = nacosTemplate.createNamespaces(namespace, "");

            if (!flag){
                log.debug("create namespace fail");
            }
            nameSpaces.put(namespace, namespace);
            nacosTemplate.serverProxys.put(namespace, nacosTemplate.createServerProxy(nameSpaces.get(namespace), namespace));
        }
        //判断service是否存在，不存在则创建
        if (!services.getOrDefault(nameSpaces.get(namespace), new HashSet<>()).contains(serviceName)){
            String flag = nacosTemplate.createService(serviceName,null,nameSpaces.get(namespace),0,null, null);
            if (!flag.equals("ok")){
                log.debug("create service fail");
            }
            services.getOrDefault(nameSpaces.get(namespace), new HashSet<>()).add(serviceName);
        }

        if (object.getSubsets()==null){
            return;
        }
        for (V1EndpointSubset sub :object.getSubsets()){
            if (sub.getAddresses()==null){
                continue;
            }
            for (V1EndpointAddress address : sub.getAddresses()) {
                for (CoreV1EndpointPort port : sub.getPorts()) {
                    Instance instance = new Instance();
                    instance.setServiceName(serviceName);
                    instance.setClusterName(clusterName);
                    instance.setIp(address.getIp());
                    instance.setPort(port.getPort());

                    nacosTemplate.createInstance(nameSpaces.get(namespace), instance);
                    NamingHttpClientProxy serverProxy = nacosTemplate.serverProxys.get(namespace);
                    try {
                        BeatInfo beatInfo = serverProxy.getBeatReactor().buildBeatInfo(serviceName, instance);
                        serverProxy.sendBeat(beatInfo, true);
                        serverProxy.getBeatReactor().addBeatInfo(beatInfo.getServiceName(), beatInfo);
                    } catch (NacosException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }



}
