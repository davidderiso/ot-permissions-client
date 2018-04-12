package com.opentable.permissions.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize
@Component
public class PermissionsClientConfig {

    @Value("${ot.permissionsClient.serviceId")
    private String permissionServiceId;
    @Value("${ot.permissionsClient.serviceUrl")
    private String permissionServiceUrl;

    @Value("${ot.permissionsClient.oauthServiceId")
    private String oauthServiceId;
    @Value("${ot.permissionsClient.oauthServiceUrl")
    private String oauthServiceUrl;

    @Value("${ot.permissionsClient.clientId")
    private String clientId;
    @Value("${ot.permissionsClient.clientSecret")
    private String clientSecret;
}
