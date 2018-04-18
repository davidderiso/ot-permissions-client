# ot-permissions-client
Java Client library for permissions service

This library provide a client to interact with the permissios service (https://github.com/opentable/service-rlc-permissions).

To use it:
Include in the pom:

  <dependency>
      <groupId>com.opentable.permissions</groupId>
      <artifactId>ot-permissions-client</artifactId>
      <version>1.0.1</version>
  </dependency>
    
  In your code you cab Autowired:
  PermissionsClient permissionsClient;
  
  the use it:
  Mono<PrincipalPermissionsResponse> perms =permissionsClient.getPrincipalPermissions(
                                          new Urn ("ot.restaurant","1"),
                                          new Urn ("ot.restaurant.user","123"));
                                        
  The library uses te reactive stream libraries, all the methods returns a Mono<>.
  
  To use it:
  
  permsResponse.flatMap( response -> doSomething(response));
  
  
  
