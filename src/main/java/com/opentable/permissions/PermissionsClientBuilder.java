package com.opentable.permissions;


import com.opentable.permissions.client.PermissionsClient;
import org.springframework.context.annotation.Configuration;

import javax.inject.Provider;

@Configuration
public class PermissionsClientBuilder implements Provider<PermissionsClient> {

    private final PermissionsClientConfiguration configClient;

    public PermissionsClientBuilder (PermissionsClientConfiguration configClient) {
        this.configClient = configClient;
    }

    @Override
    public synchronized PermissionsClient get() {

        return new PermissionsClient(configClient.config());

    }

}

