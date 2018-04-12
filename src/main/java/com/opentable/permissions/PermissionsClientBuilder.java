package com.opentable.permissions;


import com.opentable.permissions.client.PermissionsClient;
import com.opentable.permissions.discovery.PermsClientDiscoveryService;
import com.opentable.permissions.model.PermissionsClientConfig;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.inject.Provider;

@Configuration
public class PermissionsClientBuilder implements Provider<PermissionsClient> {

    private final PermissionsClientConfig configClient;

    private final PermsClientDiscoveryService permsClientDiscoveryService;

    @Inject
    public PermissionsClientBuilder (PermissionsClientConfig configClient,
                                     PermsClientDiscoveryService permsClientDiscoveryService) {
        this.configClient = configClient;
        this.permsClientDiscoveryService= permsClientDiscoveryService;
    }

    @Override
    public synchronized PermissionsClient get() {

        return new PermissionsClient(configClient,
                                     permsClientDiscoveryService);

    }

}

