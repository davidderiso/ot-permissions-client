package com.opentable.permissions.client;


//import com.opentable.metrics.DefaultMetricsConfiguration;
import com.opentable.permissions.discovery.PermsClientDiscoveryService;
import com.opentable.permissions.model.PermissionsClientConfig;
import com.opentable.service.discovery.client.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableDiscoveryClient
@ComponentScan
@EnableWebFlux
@Configuration
@Import(value = {
        PermsClientDiscoveryService.class,
        PermissionsClient.class,
        PermissionsClientConfig.class
})
public class PermissionsClientConfiguration {

}
