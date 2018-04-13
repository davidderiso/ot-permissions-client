package com.opentable.permissions;

import com.opentable.permissions.client.PermissionsClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermissionsClientFactory implements FactoryBean<PermissionsClient> {

    private final PermissionsClientConfiguration permissionsClientConfig;

    public PermissionsClientFactory (PermissionsClientConfiguration permissionsClientConfig) {

        this.permissionsClientConfig = permissionsClientConfig;

    }

    @Override
    public PermissionsClient getObject () {

        return new PermissionsClient(permissionsClientConfig.config());

    }

    @Override
    public Class<?> getObjectType() {
        return PermissionsClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
