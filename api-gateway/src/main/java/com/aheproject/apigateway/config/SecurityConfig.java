package com.aheproject.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        // disable csrf
        http.csrf().disable();

        // Permit all access for eureka
        http.authorizeExchange().pathMatchers("/erueka/**").permitAll();

        // Authorize all other requests
        http.authorizeExchange().anyExchange().authenticated();

        // Enable OAuth2 Resource Server
        http.oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);

        return http.build();
    }

}
