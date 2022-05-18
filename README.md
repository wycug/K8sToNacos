# K8sToNacos


同步k8s endpoint数据到同步到nacos instance

com.k8s.k8s.K8sTemplate.java
使用k8s APi操作k8s

com.k8s.nacos.NacosTemplate.java
使用nacos api 操作k8s

com.k8s.watch.WatchK8sToNacos.java
使用k8s的watch功能监控pod的状态，同步到nacos中



com.k8s.main.java
服务入口

目前只实现了，从k8s读取实例数据，同步到nacos中，并创建定时任务发生实例心跳。
