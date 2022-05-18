package com.k8s;

import com.k8s.K8s.K8sTemplate;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class K8sJavaClientApplicationTests {




    @Test
    void testK8sHandleTools(){
//        V1PodList v1PodList =  k8STemplate.getAllPodList(k8STemplate);
//        for(V1Pod v1Pod: v1PodList.getItems()){
//            log.info(v1Pod.toString());
//        }
    }
}
