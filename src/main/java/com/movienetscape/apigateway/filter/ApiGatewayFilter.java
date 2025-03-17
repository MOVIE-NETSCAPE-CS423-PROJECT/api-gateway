package com.movienetscape.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;


import java.util.Set;

@Component
public class ApiGatewayFilter extends AbstractGatewayFilterFactory<ApiGatewayFilter.Config> {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final Set<String> UNPROTECTED_PATHS = Set.of(
            "/api/v1/auth/login",
            "/api/v1/auth/create",
            "/api/v1/auth/forgot-password/**",
            "/api/v1/auth/change-password",
            "/api/v1/auth/verify-password-reset-token",
            "/api/v1/users/register",
            "/api/v1/users/verify"

    );

    public ApiGatewayFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();


            boolean isUnprotected = UNPROTECTED_PATHS.stream()
                    .anyMatch(pattern ->  pathMatcher.match(pattern, path));

            if (isUnprotected) {
                return chain.filter(exchange);
            }
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
