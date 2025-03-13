package com.movienetscape.apigateway.config;


import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;
import reactor.core.publisher.Mono;

import java.time.Duration;


@Configuration
public class CircuitbreakerConfig {

    private static final Logger logger = LoggerFactory.getLogger(CircuitbreakerConfig.class);

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(5)) // Set timeout to 5 seconds
                        .build())
                .build());
    }

    @Bean
    public SpringCloudCircuitBreakerFilterFactory circuitBreakerFilterFactory(
            ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory,
            ObjectProvider<DispatcherHandler> dispatcherHandlerProvider) {
        return new SpringCloudCircuitBreakerFilterFactory(circuitBreakerFactory, dispatcherHandlerProvider) {

            @Override
            protected Mono<Void> handleErrorWithoutFallback(Throwable t, boolean resumeWithoutError) {
                logger.error("Circuit breaker triggered: {}", t.getMessage(), t);
                return Mono.error(t);
            }
        };
    }
}