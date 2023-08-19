package com.dev.vault.APIGateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("authentication-route", predicateSpec -> predicateSpec
                        .path("/api/v1/auth/**")
                        .uri("lb://AUTHENTICATION-SERVICE")
                ).route("demo-route", predicateSpec -> predicateSpec
                        .path("/authenticated/**")
                        .uri("lb://AUTHENTICATION-SERVICE")
                )
                .build();
    }

}
