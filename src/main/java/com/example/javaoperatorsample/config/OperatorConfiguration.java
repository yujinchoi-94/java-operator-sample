package com.example.javaoperatorsample.config;

import java.util.List;

import com.example.javaoperatorsample.reconciler.ElevenStAppReconciler;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OperatorConfiguration {

    @Bean
    public ElevenStAppReconciler exposedAppReconciler(KubernetesClient kubernetesClient) {
        return new ElevenStAppReconciler(kubernetesClient);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Operator operator(List<Reconciler> controllers) {
        Operator operator = new Operator();
        controllers.forEach(operator::register);
        return operator;
    }

    @Bean
    public KubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }
}
