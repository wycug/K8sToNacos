package com.k8s.K8s.example.kubectl;

import io.kubernetes.client.extended.kubectl.Kubectl;
import io.kubernetes.client.extended.kubectl.exception.KubectlException;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;

import java.io.IOException;
import java.util.List;

/**
 * @auther wy
 * @create 2022/5/13 11:23
 */
public class test {
    public static void main(String[] args) throws IOException, KubectlException {
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);
//        Configuration.setDefaultApiClient(ClientBuilder.defaultClient());

        // kubectl get -n default pod foo
//        V1Pod pod = Kubectl.get(V1Pod.class)
//                .namespace("default")
//                .name("foo")
//                .execute();
// kubectl get -n default pod
//        List<V1Pod> pods = Kubectl.get(V1Pod.class)
//                .namespace("default")
//                .execute();
// kubectl get pod --all-namespaces
        List<V1Pod> pods1 = Kubectl.get(V1Pod.class)
                .execute();
        Kubectl.apiResources();
        System.out.println(pods1);

    }
}
