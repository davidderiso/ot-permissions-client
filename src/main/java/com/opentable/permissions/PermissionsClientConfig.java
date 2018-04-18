package com.opentable.permissions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize
public class PermissionsClientConfig {

    private String permissionServiceId;

    private String permissionServiceUrl;

    private String oauthServiceId;

    private String oauthServiceUrl;

    private String clientId;
    private String clientSecret;
}
