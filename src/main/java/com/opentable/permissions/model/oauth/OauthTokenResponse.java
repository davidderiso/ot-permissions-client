package com.opentable.permissions.model.oauth;

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
public class OauthTokenResponse {
    private String access_token;
    private String token_type;
    private Long expires_in;
    private String scope;
    private Long expires_at;
}
