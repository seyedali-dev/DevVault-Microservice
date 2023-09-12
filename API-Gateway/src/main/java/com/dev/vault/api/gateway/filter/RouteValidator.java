package com.dev.vault.api.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login", "/api/v1/auth/authenticate",
            "/api/v1/auth/accountVerification/",
            "/eureka"
    );

    public final Predicate<ServerHttpRequest> isSecured
            = serverHttpRequest -> openApiEndpoints
            .stream().noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));

}
