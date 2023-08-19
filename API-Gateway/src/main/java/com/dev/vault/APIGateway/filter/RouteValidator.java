package com.dev.vault.APIGateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RouteValidator {

    public final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/login", "/auth/authenticate",
            "/accountVerification/",
            "/eureka"
    );

}
