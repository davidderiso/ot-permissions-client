package com.opentable.permissions.client.oauth;

import com.google.common.base.Strings;
import com.opentable.httpheaders.OTHeaders;
import com.opentable.permissions.discovery.PermsClientDiscoveryService;
import com.opentable.permissions.model.oauth.OauthTokenResponse;
import com.opentable.service.discovery.client.DiscoveryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.UriBuilder;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class OauthClient {

    private static final String GET_TOKEN_ENDPOINT = "/api/v2/oauth/token";

    private static final String GRANT_TYPE = "grant_type";

    private final WebClient client = WebClient.create();

    private static final Logger LOG = LoggerFactory.getLogger(OauthClient.class);

    private OauthTokenResponse oauthTokenResponse = null;

    private final String clientId;

    private final String clientSecret;

    private final String oauthServiceId;
    private final String oauthServiceUrl;

    private PermsClientDiscoveryService discoveryClient=null;

    public OauthClient(String clientId,
                       String clientSecret,
                       String oauthServiceId,
                       String oauthServiceUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.oauthServiceUrl = oauthServiceUrl;
        this.oauthServiceId = oauthServiceId;
    }

    public Mono<OauthTokenResponse> getOauthToken() {
        Long nowLong = java.time.Instant.now().getEpochSecond();
        if (!Objects.isNull(oauthTokenResponse) &&
            oauthTokenResponse.getExpires_at() > nowLong) {
            return Mono.just(oauthTokenResponse);

        }

        String getTokenhUrl = UriBuilder.fromUri(getOauthUrl())
                .path(GET_TOKEN_ENDPOINT)
                .build()
                .toString();

        LOG.info("getting oauth token");
        String basicToken = "";
        try {
            basicToken = "Basic " + Base64
                    .getEncoder()
                    .encodeToString((clientId +
                                     ":" +
                                     clientSecret)
                                            .getBytes("utf-8"));
        } catch (Exception e) {
            return Mono.error(e);
        }

        return client.post()
                .uri(getTokenhUrl)
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

    public void setDiscoveryClient(DiscoveryClient discoveryClient){
        this.discoveryClient = new PermsClientDiscoveryService(discoveryClient);
    }

    private String getOauthUrl() {
        if (!Strings.isNullOrEmpty(oauthServiceId) &&
            !Objects.isNull(discoveryClient)) {
            return getServiceUrlFromDiscovery(oauthServiceId,
                                              oauthServiceUrl);
        } else {
            return oauthServiceUrl;
        }
    }

    private String getServiceUrlFromDiscovery(String serviceId, String alternateUrl) {
        if (discoveryClient != null) {
            return discoveryClient.lookUpServiceHost(serviceId)
                    .orElse(alternateUrl);
        }

        return alternateUrl;
    }


}
