package com.k8s.K8s.example;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;

import java.io.IOException;
/**
 * @auther wy
 * @create 2022/5/12 22:20
 */
public class Example {
    public static void main(String[] args) throws IOException, ApiException {
//        ApiClient client = new ClientBuilder().setBasePath("https://172.1.2.200:6443").setVerifyingSsl(false)
//                .setAuthentication(new AccessTokenAuthentication("91hwa3.0skveongyocjkvm3")).build();
        ApiClient client = Config.defaultClient();
        Configuration.setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();
        V1PodList list =
                api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        for (V1Pod item : list.getItems()) {
            System.out.println(item.getMetadata().getName());
        }
    }
}