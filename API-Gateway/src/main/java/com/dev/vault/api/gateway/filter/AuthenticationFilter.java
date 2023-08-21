package com.dev.vault.api.gateway.filter;

import com.dev.vault.api.gateway.config.jwt.JwtService;
import com.dev.vault.shared.lib.exceptions.AuthenticationFailedException;
import com.dev.vault.shared.lib.exceptions.MissingAuthenticationHeaderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    public static class Config {
    }

    public AuthenticationFilter() {
        super(Config.class);
    }


    @Value("${token.prefix}")
    private String TOKEN_PREFIX;

    private RouteValidator routeValidator;
    private JwtService jwtService;


    @Override
    public GatewayFilter apply(Config config) {
        // 1. first see if it is one of the secured urls
        // 2. check whether the request contains the header or not
        // 3. if it contains, do the validate token call
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {

                //check whether the request contains the header or not
                if (!exchange.getRequest().getHeaders().containsKey(AUTHORIZATION)) {
                    log.error("👮🏻❌ Missing Authentication Header ❌👮🏻");
                    throw new MissingAuthenticationHeaderException("👮🏻❌ Missing Authentication Header ❌👮🏻", UNAUTHORIZED);
                }

                //if it contains the key, then get the header
                String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
                String token = null;

                if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX))
                    token = authHeader.substring(TOKEN_PREFIX.length());

                try {

                    jwtService.validateToken(token);
                    log.info("✅ Token is valid! ✅");

                } catch (Exception ex) {
                    log.error("👮🏻❌ Unauthorized access to endpoints. Token is not valid.. ❌👮🏻");
                    throw new AuthenticationFailedException("👮🏻❌ Unauthorized access to endpoints. Token is not valid.. ❌👮🏻 ::: " + ex.getMessage(), UNAUTHORIZED);
                }

            }
            return chain.filter(exchange);
        };
    }


    public @Autowired void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public @Autowired void setRouteValidator(RouteValidator routeValidator) {
        this.routeValidator = routeValidator;
    }

}
