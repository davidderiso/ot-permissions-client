package com.opentable.permissions.discovery;

//import com.opentable.service.discovery.client.DiscoveryClient;
//import com.opentable.service.discovery.client.ServiceLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class PermsClientDiscoveryService {
    private static final Logger LOG = LoggerFactory.getLogger(PermsClientDiscoveryService.class);

  //  private final DiscoveryClient discoveryClient;

    //public PermsClientDiscoveryService(DiscoveryClient discoveryClient) {
      //  this.discoveryClient = discoveryClient;

        //LOG.info("Discovery Client: {}", discoveryClient);
    //}

    public Optional<String> lookUpServiceHost(String serviceId) {
        LOG.debug("looking up host for serviceId: {}", serviceId);
        return Optional.empty();
    }

//        return Optional.ofNullable(discoveryClient
//                                           .findAnnouncement(ServiceLookup.with()
//                                                                     .serviceType(serviceId)))
//                .map(a->a.getServiceUri().toString());
//    }
}
