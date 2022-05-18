package com.k8s.nacos;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.CommonParams;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import com.alibaba.nacos.client.naming.beat.BeatInfo;
import com.alibaba.nacos.client.naming.cache.ServiceInfoHolder;
import com.alibaba.nacos.client.naming.core.ServerListManager;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientManager;
import com.alibaba.nacos.client.naming.remote.http.NamingHttpClientProxy;
import com.alibaba.nacos.client.naming.utils.UtilAndComs;
import com.alibaba.nacos.client.security.SecurityProxy;
import com.alibaba.nacos.common.utils.HttpMethod;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.k8s.nacos.bean.NameSpcaesBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.SpringProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

import static com.alibaba.nacos.client.utils.LogUtils.NAMING_LOGGER;

/**
 * @auther wy
 * @create 2022/5/13 15:24
 */
public class NacosTemplate {

    private static final String ip = "http://localhost:8848";

//    private NamingService namingService;
//    private NamingMaintainService maintainService;



    public Map<String, NamingHttpClientProxy> serverProxys;
    private final NamingHttpClientManager namingHttpClientManager;
    private final NamingHttpClientProxy defaultProxy;

    private Properties prop;

    public NacosTemplate(){

//        try {
//            namingService = NacosFactory.createNamingService(ip);
//            maintainService = NacosFactory.createMaintainService(ip);
//        } catch (NacosException e) {
//            e.printStackTrace();
//        }
        serverProxys = new HashMap<>();
        namingHttpClientManager = NamingHttpClientManager.getInstance();
        initProp();
        defaultProxy = createServerProxy("", "public");
    }

    public void initProp(){
        prop = new Properties();
        prop.setProperty("username", "nacos");
        prop.setProperty("password", "nacos");
        prop.setProperty("contextPath", "http://localhost:8848");
        prop.setProperty("serverAddr", "http://localhost:8848");
        prop.setProperty("namingCacheRegistryDir", "/data/");
        prop.setProperty("namingRequestDomainMaxRetryCount", "10");
    }
    public NamingHttpClientProxy createServerProxy(String namespaceId, String namespace){

        ServerListManager serverListManager = new ServerListManager(prop);
        SecurityProxy securityProxy = new SecurityProxy(prop, namingHttpClientManager.getNacosRestTemplate());
        return new NamingHttpClientProxy(namespaceId, securityProxy, serverListManager, prop, new ServiceInfoHolder(namespace, prop));

    }


    /**
     * 获取所有的命名空间
     * @return
     */
    public Map<String, String> getNamespaces(){
        String path = "/nacos/v1/console/namespaces";
        Map<String, String> params = new HashMap(8);
        Map<String, String> res = new HashMap<>();
        try {
            String result = defaultProxy.reqApi(path, params, "GET");
            JsonNode jsonNode = JacksonUtils.toObj(result);
            System.out.println(jsonNode);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 创建命名空间
     * @param namespaceName
     * @param namespaceDesc
     * @return
     */

    public Boolean createNamespaces(String namespaceName,String namespaceDesc){
        String path = "/nacos/v1/console/namespaces";
        Map<String, String> params = new HashMap<>();
        params.put("customNamespaceId", namespaceName);
        params.put("namespaceName", namespaceName);
        params.put("namespaceDesc", namespaceDesc);
        try {
            String res = defaultProxy.reqApi(path, params, "POST");
            System.out.println(res);
        } catch (NacosException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /***
     * serviceName	字符串	是	服务名
     * groupName	字符串	否	分组名
     * namespaceId	字符串	否	命名空间ID
     * @param serviceName
     * @param groupName
     * @param namespaceId
     * @return
     */

    public Service getService(String serviceName, String groupName, String namespaceId){
        String path = "/nacos/v1/ns/service?serviceName";
        Map<String, String> params = new HashMap<>();
        params.put("serviceName", serviceName);
        params.put("groupName", groupName);
        params.put("namespaceId", namespaceId);
        Service service = new Service();
        try {
            String res = defaultProxy.reqApi(path, params, "GET");
            System.out.println(res);
        } catch (NacosException e) {
            e.printStackTrace();

        }
        return service;
    }


    /***
     * serviceName	字符串	是	服务名
     * groupName	字符串	否	分组名
     * namespaceId	字符串	否	命名空间ID
     * protectThreshold	浮点数	否	保护阈值,取值0到1,默认0
     * metadata	字符串	否	元数据
     * selector	JSON格式字符串	否	访问策略
     * @param serviceName
     * @return
     */

    public String createService(String serviceName, String groupName, String namespaceId, float protectThreshold, String metadata, String selector){
        String path = "/nacos/v1/ns/service";
        Map<String, String> params = new HashMap<>();
        params.put("serviceName", serviceName);
        params.put("groupName", groupName);
        params.put("namespaceId", namespaceId);
        params.put("protectThreshold", String.valueOf(protectThreshold));
        params.put("metadata", metadata);
        params.put("selector", selector);
        try {
            String post = defaultProxy.reqApi(path, params, "POST");
            System.out.println(post);
        } catch (NacosException e) {
            e.printStackTrace();
        }

        return "fail";
    }




    /***
     *      *     ip	字符串	是	服务实例IP
     *      *     port	int	是	服务实例port
     *      *     namespaceId	字符串	否	命名空间ID
     *      *     weight	double	否	权重
     *      *     enabled	boolean	否	是否上线
     *      *     healthy	boolean	否	是否健康
     *      *     metadata	字符串	否	扩展信息
     *      *     clusterName	字符串	否	集群名
     *      *     serviceName	字符串	是	服务名
     *      *     groupName	字符串	否	分组名
     *      *     ephemeral	boolean	否	是否临时实例
     * @param namespaceId
     * @param instance
     * @return
     */


    public String createInstance(String namespaceId,Instance instance){
//        String sip, int port, String namespaceId,double weight, Boolean enabled,Boolean healthy, String metadata, String clusterName, String serviceName, String groupName, Boolean ephemeral
        String path = ip + "/nacos/v1/ns/instance";
        Map<String, String> params = new HashMap<>();
        params.put("ip", instance.getIp());
        params.put("port", String.valueOf(instance.getPort()));
        params.put("namespaceId", namespaceId);
        params.put("weight", String.valueOf(instance.getWeight()));
        params.put("enabled", String.valueOf(instance.isEnabled()));
        params.put("healthy", String.valueOf(instance.isHealthy()));
        params.put("metadata", instance.getMetadata().toString());
        params.put("clusterName", instance.getClusterName());
        params.put("serviceName", instance.getServiceName());
        params.put("groupName", "");
        params.put("ephemeral", String.valueOf(instance.isEphemeral()));

        try {
            String res = defaultProxy.reqApi(path, params, "POST");
            System.out.println(res);
        }catch (Exception e){

        }

        return "fail";
    }

}