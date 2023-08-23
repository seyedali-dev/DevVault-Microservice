package com.dev.vault.api.gateway.config;

import com.dev.vault.api.gateway.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                // AUTHENTICATION-SERVICE
                .route("authentication-service-route", predicateSpec -> predicateSpec
                        .path("/api/v1/auth/**")
                        .uri("lb://AUTHENTICATION-SERVICE")
                )
                .route("demo-route", predicateSpec -> predicateSpec
                        .path("/authenticated/**")
                        .uri("lb://AUTHENTICATION-SERVICE")
                )

                // PROJECT-SERVICE
                .route("project-route", predicateSpec -> predicateSpec
                        .path("/api/v1/project/proj_leader/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filterWithAuthentication()))
                        .uri("lb://PROJECT-SERVICE")
                )
                .build();
    }

    private GatewayFilter filterWithAuthentication() {
        return (exchange, chain) -> authenticationFilter.apply((AuthenticationFilter.Config) null).filter(exchange, chain);
    }

}
