package com.opentable.permissions;

//import com.opentable.permissions.client.PermissionsClient;
import com.opentable.permissions.client.PermissionsClient;
import com.opentable.permissions.discovery.PermsClientDiscoveryService;
import com.opentable.permissions.model.PermissionsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Configuration
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({PermissionsClientConfiguration.class,
        PermissionsClientBuilder.class,
        PermissionsClient.class,
        PermissionsClientConfig.class,
        PermsClientDiscoveryService.class})
public @interface EnablePermissionsClient {

}
