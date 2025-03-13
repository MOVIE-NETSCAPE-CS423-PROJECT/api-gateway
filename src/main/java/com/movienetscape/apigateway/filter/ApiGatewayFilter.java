package com.movienetscape.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
public class ApiGatewayFilter extends AbstractGatewayFilterFactory<ApiGatewayFilter.Config> {

    public ApiGatewayFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            System.out.println("authHeader: " + authHeader);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            System.out.println("authHeader: " + authHeader);
            return chain.filter(exchange);
        };
    }


    public static class Config {
    }
}


