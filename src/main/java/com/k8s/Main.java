package com.k8s;

import com.k8s.K8s.K8sTemplate;
import com.k8s.watch.WatchK8sToNacos;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Watch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Objects;

/**
 * @auther wy
 * @create 2022/5/14 20:24
 */
public class Main {

    public static void main(String[] args) throws IOException, ApiException {
        WatchK8sToNacos watchK8sToNacos = new WatchK8sToNacos();
        watchK8sToNacos.watch();
    }


}
