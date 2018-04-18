package com.opentable.permissions.discovery;

import com.opentable.service.discovery.client.DiscoveryClient;
import com.opentable.service.discovery.client.ServiceLookup;

import java.util.Optional;

public class PermsClientDiscoveryService {

    private final DiscoveryClient discoveryClient;

    public PermsClientDiscoveryService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public Optional<String> lookUpServiceHost(String serviceId) {
        return Optional.ofNullable(discoveryClient
                                           .findAnnouncement(ServiceLookup.with()
                                                                     .serviceType(serviceId)))
                .map(a->a.getServiceUri().toString());
    }
}
