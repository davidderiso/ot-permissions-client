# ot-permissions-client
Java Client library for permissions service

This library provide a client to interact with the permissios service (https://github.com/opentable/service-rlc-permissions).

To use the client"
Include in the pom:  
  
      <dependency>
          <groupId>com.opentable.permissions</groupId>
          <artifactId>ot-permissions-client</artifactId>
          <version>1.0.1</version>
      </dependency>

if you need the reactive library, you will need to include:

      <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
            <version>3.1.5.RELEASE</version>
      </dependency>
 
 Your configuration file will need to have the following variables:
 
* `ot.permissionsClient.serviceId`: the permissions service id (should be service-rlc-permissions).Optional if you specify the url
* `ot.permissionsClient.serviceUrl`: the permissions service url. Optional if you specify the serviceId

* `ot.permissionsClient.oauthServiceId`: the oauth service id. Optional if you specify the oauth service url.
* `ot.permissionsClient.oauthServiceUrl`: the oauth service url. Optional if you specify the service Id.

* `ot.permissionsClient.clientId`: The service clientId (needed to autheticaterequests to permissions service)
* `ot.permissionsClient.clientSecret`: The service client secret (needed to autheticaterequests to permissions service)
   
In your code you need to enable the permission client with:
   
   `@EnablePermissionsClient`
   
Then you can Autowire:

  `PermissionsClient permissionsClient;`
  
To use it in the code:

```java
Mono<PrincipalPermissionsResponse> perms = permissionsClient.getPrincipalPermissions(
                                        new Urn ("ot.restaurant","1"), 
                                        new Urn ("ot.restaurant.user","123"));
```                                     
  
  The library uses te reactive stream libraries, all the methods returns a Mono<>.
  
  To use it:
  
  `permsResponse.flatMap( response -> doSomething(response));`
  
  
  
