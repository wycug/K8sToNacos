package com.k8s.nacos.bean;

/**
 * @auther wy
 * @create 2022/5/16 22:31
 */
public class NameSpaceBean {
    String namespace;
    String namespaceShowName;
    int quota;
    int configCount;
    int type;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespaceShowName() {
        return namespaceShowName;
    }

    public void setNamespaceShowName(String namespaceShowName) {
        this.namespaceShowName = namespaceShowName;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getConfigCount() {
        return configCount;
    }

    public void setConfigCount(int configCount) {
        this.configCount = configCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
