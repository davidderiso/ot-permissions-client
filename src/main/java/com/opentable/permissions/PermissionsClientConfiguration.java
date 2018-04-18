package com.opentable.permissions;

import com.opentable.permissions.client.PermissionsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermissionsClientConfiguration {

    @Bean
    public PermissionsClientConfig permissionsClientConfig (
            @Value("${ot.permissionsClient.serviceId}") final String permissionServiceId,
            @Value("${ot.permissionsClient.serviceUrl}") final String permissionServiceUrl,

            @Value("${ot.permissionsClient.oauthServiceId}") final String oauthServiceId,
            @Value("${ot.permissionsClient.oauthServiceUrl}") final String oauthServiceUrl,

            @Value("${ot.permissionsClient.clientId}") final String clientId,
            @Value("${ot.permissionsClient.clientSecret}") final String clientSecret) {
        return PermissionsClientConfig
                .builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .oauthServiceId(oauthServiceId)
                .oauthServiceUrl(oauthServiceUrl)
                .permissionServiceId(permissionServiceId)
                .permissionServiceUrl(permissionServiceUrl)
                .build();
    }

    @Bean
    public PermissionsClient permissionsClient(PermissionsClientConfig config) {
        return new PermissionsClient(config);
    }
}
