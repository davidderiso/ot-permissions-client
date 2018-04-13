package com.opentable.permissions.client;

import com.opentable.httpheaders.OTHeaders;
import com.opentable.permissions.discovery.PermsClientDiscoveryService;
import com.opentable.permissions.PermissionsClientConfig;
import com.opentable.permissions.model.PrincipalPermissionsResponse;
import com.opentable.permissions.model.Urn;
import com.opentable.permissions.model.oauth.OauthTokenResponse;
import com.opentable.service.discovery.client.DiscoveryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.UriBuilder;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class PermissionsClient {

    private static final String GET_TOKEN_ENDPOINT = "/api/v2/oauth/token";

    private static final String GRANT_TYPE = "grant_type";

    private final PermissionsClientConfig permissionsClientConfig;

    private PermsClientDiscoveryService permsClientDiscoveryService;

    private final WebClient client = WebClient.create();

    private static final Logger LOG = LoggerFactory.getLogger(PermsClientDiscoveryService.class);

    private OauthTokenResponse oauthTokenResponse;

    public PermissionsClient(PermissionsClientConfig permissionsClientConfig) {
        this.permissionsClientConfig = permissionsClientConfig;

    }

    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.permsClientDiscoveryService = new PermsClientDiscoveryService(discoveryClient);
    }

    public Mono<PrincipalPermissionsResponse> getPrincipalPermissions(Urn context, Urn principal) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path("/api/v1/contexts/" + context.toString() + "/principals/"+principal.toString()+"/permissions")
                .build()
                .toString();

        return getFromPermissions(url)
                .flatMap(clientResponse -> clientResponse.bodyToMono(PrincipalPermissionsResponse.class));
    }


    private Mono<OauthTokenResponse> getOauthToken() {

        Long nowLong = java.time.Instant.now().getEpochSecond();
        if (!Objects.isNull(oauthTokenResponse) &&
            oauthTokenResponse.getExpires_at() > nowLong) {
            return Mono.just(oauthTokenResponse);

        }

        String oauthUrl = UriBuilder.fromUri(getOauthUrl())
                .path(GET_TOKEN_ENDPOINT)
                .build()
                .toString();

        String basicToken = "";
        try {
            basicToken = "Basic " + Base64
                    .getEncoder()
                    .encodeToString((permissionsClientConfig.getClientId() +
                                     ":" +
                                     permissionsClientConfig.getClientSecret())
                                            .getBytes("utf-8"));
        } catch (Exception e) {
            return Mono.error(e);
        }

        return client.post()
                .uri(oauthUrl)
                .header(HttpHeaders.CACHE_CONTROL, CacheControl.noCache().getHeaderValue())
                .header(OTHeaders.REQUEST_ID, UUID.randomUUID().toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, basicToken)
                .body(fromFormData(GRANT_TYPE, "client_credentials"))
                .exchange()
                .flatMap(cr -> {
                    if (!cr.statusCode().is2xxSuccessful()) {
                        return Mono.error(new ResponseStatusException(cr.statusCode()));
                    }
                    return cr.bodyToMono(OauthTokenResponse.class)
                            .doAfterSuccessOrError((t, th) -> {
                                                           oauthTokenResponse = t;
                    oauthTokenResponse.setExpires_at(java.time.Instant.now().getEpochSecond() +
                                                     oauthTokenResponse.getExpires_in() * 1000);
                            });
                });
    }

    private Mono<ClientResponse> getFromPermissions(String url) {

        return getOauthToken()
                .flatMap(token -> this.client.get()
                        .uri(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccess_token())
                        .header(HttpHeaders.CACHE_CONTROL, CacheControl.noCache().getHeaderValue())
                        .header(OTHeaders.REQUEST_ID, UUID.randomUUID().toString())
                        .exchange()
                        .flatMap(cr -> {
                            if (!cr.statusCode().is2xxSuccessful()) {
                                LOG.warn("Error calling permissions service, code:" +
                                         cr.statusCode().getReasonPhrase());
                                return Mono.error(new ResponseStatusException(cr.statusCode()));
                            }
                            return Mono.just(cr);
                        })
                );
    }

    private String getPermissionsUrl() {
        if (!Objects.isNull(permissionsClientConfig.getPermissionServiceId()) &&
            !Objects.isNull(permsClientDiscoveryService)) {
            return getServiceUrlFromDiscovery(permissionsClientConfig.getPermissionServiceId(),
                                              permissionsClientConfig.getPermissionServiceUrl());
        } else {
            return permissionsClientConfig.getPermissionServiceUrl();
        }
    }

    private String getOauthUrl() {
        if (!Objects.isNull(permissionsClientConfig.getOauthServiceId()) &&
            !Objects.isNull(permsClientDiscoveryService)) {
            return getServiceUrlFromDiscovery(permissionsClientConfig.getOauthServiceId(),
                                              permissionsClientConfig.getOauthServiceUrl());
        } else {
            return permissionsClientConfig.getOauthServiceUrl();
        }
    }

    private String getServiceUrlFromDiscovery(String serviceId, String alternateUrl) {
        if (permsClientDiscoveryService != null) {
            return permsClientDiscoveryService.lookUpServiceHost(serviceId)
                    .orElse(alternateUrl);
        }

        return null;
    }


}
