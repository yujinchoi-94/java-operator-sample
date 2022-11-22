package com.example.javaoperatorsample.reconciler;

import java.util.Map;

import com.example.javaoperatorsample.cr.ElevenStApp;
import com.example.javaoperatorsample.cr.ElevenStAppStatus;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.ContainerPortBuilder;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpecBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerConfiguration
public class ElevenStAppReconciler implements Reconciler<ElevenStApp>{

    private static final Logger logger = LoggerFactory.getLogger(ElevenStAppReconciler.class);
    private final KubernetesClient kubernetesClient;

    public ElevenStAppReconciler(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public UpdateControl<ElevenStApp> reconcile(ElevenStApp resource, Context<ElevenStApp> context) {

        // CR 내용 로깅
        logger.info("Start Reconcile Logic!");
        logger.info("CRD name : " + resource.getCRDName());
        logger.info("metadata.name : " + resource.getMetadata().getName());
        logger.info("spec.appId : " + resource.getSpec().getAppId());
        logger.info("spec.owner : " +resource.getSpec().getOwner());

        // deployments 리소스 생성
        kubernetesClient.apps()
            .deployments()
            .create(new DeploymentBuilder()
                .withMetadata(new ObjectMetaBuilder()
                    .withName(resource.getSpec().getAppId() +"-nginx") // CR에 사용자가 설정한 정보를 사용
                    .withLabels(Map.of("app", "label"))
                    .build())
                .withSpec(new DeploymentSpecBuilder()
                    .withReplicas(3)
                    .withTemplate(new PodTemplateSpecBuilder()
                        .withMetadata(new ObjectMetaBuilder()
                            .withLabels(Map.of("app", "nginx"))
                            .build())
                        .withSpec(new PodSpecBuilder()
                            .withContainers(new ContainerBuilder()
                                .withName("nginx")
                                .withImage("nginx:1.14.2")
                                .withPorts(new ContainerPortBuilder().withContainerPort(80).build())
                                .build())
                            .build())
                        .build())
                    .withSelector(new LabelSelectorBuilder()
                        .withMatchLabels(Map.of("app", "nginx"))
                        .build())
                    .build())
                .build());

        // status 변경
        var status = new ElevenStAppStatus();
        status.setReady(true);
        resource.setStatus(status);

        return UpdateControl.patchStatus(resource);
    }
}
