package com.opentable.permissions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.opentable.permissions")
public class PermissionsClientConfiguration {

    @Value("${ot.permissionsClient.serviceId}")
    private String permissionServiceId;
    @Value("${ot.permissionsClient.serviceUrl}")
    private String permissionServiceUrl;

    @Value("${ot.permissionsClient.oauthServiceId}")
    private String oauthServiceId;
    @Value("${ot.permissionsClient.oauthServiceUrl}")
    private String oauthServiceUrl;

    @Value("${ot.permissionsClient.clientId}")
    private String clientId;
    @Value("${ot.permissionsClient.clientSecret}")
    private String clientSecret;

    public PermissionsClientConfig config () {
        return PermissionsClientConfig
                .builder()
                .oauthServiceUrl(oauthServiceUrl)
                .oauthServiceId(oauthServiceId)
                .permissionServiceUrl(permissionServiceUrl)
                .permissionServiceId(permissionServiceId)
                .clientSecret(clientSecret)
                .clientId(clientId)
                .build();
    }
}
