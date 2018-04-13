package com.opentable.permissions.client;

import com.google.common.base.Strings;
import com.opentable.httpheaders.OTHeaders;
import com.opentable.permissions.client.oauth.OauthClient;
import com.opentable.permissions.discovery.PermsClientDiscoveryService;
import com.opentable.permissions.PermissionsClientConfig;
import com.opentable.permissions.model.ContextsForPrincipalResponse;
import com.opentable.permissions.model.PermissionsRequestObject;
import com.opentable.permissions.model.PrincipalPermissionsResponse;
import com.opentable.permissions.model.PrincipalResponseObject;
import com.opentable.permissions.model.PrincipalRolesResponse;
import com.opentable.permissions.model.PrincipalsResponse;
import com.opentable.permissions.model.RolesInContextResponse;
import com.opentable.permissions.model.RolesRequestObject;
import com.opentable.permissions.model.Urn;
import com.opentable.service.discovery.client.DiscoveryClient;
import org.reactivestreams.Publisher;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


public class PermissionsClient {

    private static final String PERMISSION_API_CONTEXTS = "/api/v1/contexts/";

    private static final String PRINCIPALS = "/principals/";

    private final PermissionsClientConfig permissionsClientConfig;

    private PermsClientDiscoveryService permsClientDiscoveryService;

    private final WebClient client = WebClient.create();

    private final OauthClient oauthClient;

    private static final Logger LOG = LoggerFactory.getLogger(PermsClientDiscoveryService.class);

    public PermissionsClient(PermissionsClientConfig permissionsClientConfig) {
        this.permissionsClientConfig = permissionsClientConfig;
        this.oauthClient = new OauthClient(permissionsClientConfig.getClientId(),
                                           permissionsClientConfig.getClientSecret(),
                                           permissionsClientConfig.getOauthServiceId(),
                                           permissionsClientConfig.getOauthServiceUrl());

    }

    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.permsClientDiscoveryService = new PermsClientDiscoveryService(discoveryClient);
        this.oauthClient.setDiscoveryClient(discoveryClient);
    }

    public Mono<PrincipalPermissionsResponse> getPrincipalPermissions(Urn context, Urn principal) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + PRINCIPALS + principal.toString() +
                      "/permissions")
                .build()
                .toString();

        return getFromPermissions(url)
                .flatMap(clientResponse -> clientResponse.bodyToMono(PrincipalPermissionsResponse.class));
    }

    public Mono<PrincipalRolesResponse> getPrincipalRoles(Urn context, Urn principal) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + PRINCIPALS + principal.toString() + "/roles")
                .build()
                .toString();

        return getFromPermissions(url)
                .flatMap(clientResponse -> clientResponse.bodyToMono(PrincipalRolesResponse.class));
    }

    public Mono<ContextsForPrincipalResponse> getPrincipalContexts(Urn principal) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path("/api/v1/principals/" + principal.toString() + "/contexts")
                .build()
                .toString();

        return getFromPermissions(url)
                .flatMap(clientResponse -> clientResponse.bodyToMono(ContextsForPrincipalResponse.class));
    }

    public Mono<RolesInContextResponse> getRolesInContext(Urn context) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + "/roles")
                .build()
                .toString();

        return getFromPermissions(url)
                .flatMap(clientResponse -> clientResponse.bodyToMono(RolesInContextResponse.class));
    }

    public Mono<PrincipalsResponse> getPrincipalsInContext(Urn context) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + PRINCIPALS)
                .build()
                .toString();

        return getFromPermissions(url)
                .flatMap(clientResponse -> clientResponse.bodyToMono(PrincipalsResponse.class));
    }

    public Mono<PrincipalResponseObject> getPrincipalInfo(Urn context, Urn principal) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + "/principal/" + principal.toString())
                .build()
                .toString();

        return getFromPermissions(url)
                .flatMap(clientResponse -> clientResponse.bodyToMono(PrincipalResponseObject.class));
    }

    public Mono<PrincipalPermissionsResponse> updatePermissionsToRole(Urn context,
                                                                      Urn role,
                                                                      List<Urn> permissions) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + "/roles/" + role.toString() + "/permissions")
                .build()
                .toString();

        PermissionsRequestObject req = PermissionsRequestObject.builder()
                .permissions(permissions.stream().map(Urn::toString).collect(Collectors.toList()))
                .build();

        return putToPermissions(url, Mono.just(req), PermissionsRequestObject.class)
                .flatMap(cr -> cr.bodyToMono(PrincipalPermissionsResponse.class));
    }

    public Mono<PrincipalRolesResponse> updateRolesForPrincipal(Urn context,
                                                                Urn principal,
                                                                List<Urn> roles) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + PRINCIPALS + principal.toString() + "/roles")
                .build()
                .toString();

        RolesRequestObject req = RolesRequestObject.builder()
                .roles(roles.stream().map(Urn::toString).collect(Collectors.toList()))
                .build();

        return putToPermissions(url, Mono.just(req), RolesRequestObject.class)
                .flatMap(cr -> cr.bodyToMono(PrincipalRolesResponse.class));
    }

    public Mono<PrincipalPermissionsResponse> updatePermissionsForPrincipal(Urn context,
                                                                            Urn principal,
                                                                            List<Urn> permissions) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + PRINCIPALS + principal.toString() +
                      "/permissions")
                .build()
                .toString();

        PermissionsRequestObject req = PermissionsRequestObject.builder()
                .permissions(permissions.stream().map(Urn::toString).collect(Collectors.toList()))
                .build();

        return putToPermissions(url, Mono.just(req), PermissionsRequestObject.class)
                .flatMap(cr -> cr.bodyToMono(PrincipalPermissionsResponse.class));
    }

    public Mono<ClientResponse> deletePrincipalPermission(Urn context, Urn principal, Urn permission) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + PRINCIPALS + principal.toString() +
                      "/permissions/" + permission.toString())
                .build()
                .toString();

        return deleteFromPermissions(url);
    }

    public Mono<ClientResponse> deletePrincipalRole(Urn context, Urn principal, Urn role) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + PRINCIPALS + principal.toString() +
                      "/roles/" + role.toString())
                .build()
                .toString();

        return deleteFromPermissions(url);
    }

    public Mono<ClientResponse> deleteRolePermission(Urn context, Urn role, Urn permission) {
        String url = UriBuilder.fromUri(getPermissionsUrl())
                .path(PERMISSION_API_CONTEXTS + context.toString() + "/roles/" + role.toString() +
                      "/permissions/" + permission.toString())
                .build()
                .toString();

        return deleteFromPermissions(url);
    }

    private Mono<ClientResponse> deleteFromPermissions(String url) {
        return oauthClient.getOauthToken()
                .flatMap(token -> this.client.delete()
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

    private Mono<ClientResponse> getFromPermissions(String url) {

        return oauthClient.getOauthToken()
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

    private <T, P extends Publisher<T>> Mono<ClientResponse> putToPermissions(String url, P publisher,
                                                                              Class<T> elementClass) {

        return oauthClient.getOauthToken()
                .flatMap(token -> this.client.put()
                        .uri(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccess_token())
                        .header(HttpHeaders.CACHE_CONTROL, CacheControl.noCache().getHeaderValue())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .header(OTHeaders.REQUEST_ID, UUID.randomUUID().toString())
                        .body(publisher, elementClass)
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
        if (!Strings.isNullOrEmpty((permissionsClientConfig.getPermissionServiceId())) &&
            !Objects.isNull(permsClientDiscoveryService)) {
            return getServiceUrlFromDiscovery(permissionsClientConfig.getPermissionServiceId(),
                                              permissionsClientConfig.getPermissionServiceUrl());
        } else {
            return permissionsClientConfig.getPermissionServiceUrl();
        }
    }

    private String getServiceUrlFromDiscovery(String serviceId, String alternateUrl) {
        if (permsClientDiscoveryService != null) {
            return permsClientDiscoveryService.lookUpServiceHost(serviceId)
                    .orElse(alternateUrl);
        }

        return alternateUrl;
    }


}
