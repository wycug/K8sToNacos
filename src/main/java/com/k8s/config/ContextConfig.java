package com.k8s.config;

import com.k8s.K8s.K8sTemplate;
import com.k8s.nacos.NacosTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @auther wy
 * @create 2022/5/16 16:20
 */
@Configuration
public class ContextConfig {
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
