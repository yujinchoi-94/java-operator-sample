package com.example.javaoperatorsample.cr;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1")
@Group("11st.example.com")
public class ElevenStApp extends CustomResource<ElevenStAppSpec, ElevenStAppStatus> implements Namespaced {
}

