package com.opentable.permissions;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import({PermissionsClientConfiguration.class,
        PermissionsClientBuilder.class,
        PermissionsClientFactory.class})
public @interface EnablePermissionsClient {

}
