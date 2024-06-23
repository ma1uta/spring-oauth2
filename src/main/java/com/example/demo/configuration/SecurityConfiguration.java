package com.example.demo.configuration;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OidcJwtConverter oidcJwtConverter) throws Exception {
        http.authorizeHttpRequests((authz) -> authz.requestMatchers("/api/**").authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt((jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(oidcJwtConverter))));
        return http.build();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public OidcJwtConverter oidcJwtConverter(OAuth2ResourceServerProperties properties, RestClient.Builder restClientBuilder) {
        return new OidcJwtConverter(properties, restClientBuilder);
    }
}
