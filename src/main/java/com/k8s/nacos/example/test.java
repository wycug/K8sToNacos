package com.k8s.nacos.example;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.k8s.nacos.NacosTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @auther wy
 * @create 2022/5/13 15:23
 */
@Slf4j
public class test {
    private static ServerNode serverNode = new ServerNode("http://173.1.2.203",31303,"test");
    public static void main(String[] args) throws Exception {
        NacosTemplate nacosTemplate = new NacosTemplate();
        Instance instance = new Instance();
        instance.setIp(serverNode.getIp());
        instance.setPort(serverNode.getPort());
        instance.setServiceName(serverNode.getName());
        nacosTemplate.registerNacosInstance(instance);
        log.debug("nacos注册成功呀");
    }
}
class ServerNode{
    String ip;
    int port;
    String name;

    public ServerNode(String ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
