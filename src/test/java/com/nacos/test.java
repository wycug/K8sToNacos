package com.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;

import com.alibaba.nacos.api.naming.pojo.Cluster;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import com.alibaba.nacos.api.naming.pojo.healthcheck.AbstractHealthChecker;
import com.k8s.nacos.NacosTemplate;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther wy
 * @create 2022/5/16 15:58
 */
public class test {
//    @Test
//    public void test() throws NacosException {
//        NamingService naming = NamingFactory.createNamingService(System.getProperty("nacos.host"));
//        naming.registerInstance("nacos.test.3", "11.11.11.11", 8888, "TEST1");
//
//        Instance instance = new Instance();
//        instance.setIp("55.55.55.55");
//        instance.setPort(9999);
//        instance.setHealthy(false);
//        instance.setWeight(2.0);
//        Map<String, String> instanceMeta = new HashMap<>();
//        instanceMeta.put("site", "et2");
//        instance.setMetadata(instanceMeta);
//
//        Service service = new Service("nacos.test.4");
//        service.setApp("nacos-naming");
//        service.sethealthCheckMode("server");
//        service.setEnableHealthCheck(true);
//        service.setProtectThreshold(0.8F);
//        service.setGroup("CNCF");
//        Map<String, String> serviceMeta = new HashMap<>();
//        serviceMeta.put("symmetricCall", "true");
//        service.setMetadata(serviceMeta);
//        instance.setService(service);
//
//        Cluster cluster = new Cluster();
//        cluster.setName("TEST5");
//        AbstractHealthChecker.Http healthChecker = new AbstractHealthChecker.Http();
//        healthChecker.setExpectedResponseCode(400);
//        healthChecker.setCurlHost("USer-Agent|Nacos");
//        healthChecker.setCurlPath("/xxx.html");
//        cluster.setHealthChecker(healthChecker);
//        Map<String, String> clusterMeta = new HashMap<>();
//        clusterMeta.put("xxx", "yyyy");
//        cluster.setMetadata(clusterMeta);
//
//        instance.setCluster(cluster);
//
//        naming.registerInstance("nacos.test.4", instance);
//    }

    @Test
    public void test(){
        NacosTemplate nacosTemplate = new NacosTemplate();
        Service service = nacosTemplate.getService("nacos.test.2","","");

        System.out.println(service);
    }
}
