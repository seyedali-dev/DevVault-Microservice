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
                        .uri("lb://4-AUTHENTICATION-SERVICE")
                )
                .route("demo-route", predicateSpec -> predicateSpec
                        .path("/authenticated/**")
                        .uri("lb://4-AUTHENTICATION-SERVICE")
                )

                // PROJECT-SERVICE
                .route("project-route", predicateSpec -> predicateSpec
                        .path("/api/v1/project/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filterWithAuthentication()))
                        .uri("lb://5-PROJECT-SERVICE")
                )
                .route("project-search-route", predicateSpec -> predicateSpec
                        .path("/api/v1/search_project/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filterWithAuthentication()))
                        .uri("lb://5-PROJECT-SERVICE")
                )
                .route("project-join_coupon-route", predicateSpec -> predicateSpec
                        .path("/api/v1/join_coupon/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filterWithAuthentication()))
                        .uri("lb://5-PROJECT-SERVICE")
                )
                .route("project-join_request-route", predicateSpec -> predicateSpec
                        .path("/api/v1/join_request/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filterWithAuthentication()))
                        .uri("lb://5-PROJECT-SERVICE")
                )

                //TASK-SERVICE
                .route("task-management-route", predicateSpec -> predicateSpec
                        .path("/api/v1/task/management/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filterWithAuthentication()))
                        .uri("lb://6-TASK-SERVICE")
                )
                .build();
    }

    // Used for putting the authentication filter before each specified url
    private GatewayFilter filterWithAuthentication() {
        return (exchange, chain) -> authenticationFilter.apply((AuthenticationFilter.Config) null).filter(exchange, chain);
    }

}
