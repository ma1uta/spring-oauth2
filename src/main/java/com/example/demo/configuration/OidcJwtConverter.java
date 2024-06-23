package com.example.demo.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

public class OidcJwtConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final OAuth2ResourceServerProperties properties;
    private final RestClient restClient;

    public OidcJwtConverter(OAuth2ResourceServerProperties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.restClient = restClientBuilder.build();
    }

    @Override
    public JwtAuthenticationToken convert(Jwt source) {
        final var tokenValue = source.getTokenValue();

        final var conf = properties.getJwt().getIssuerUri() + "/.well-known/openid-configuration";
        final var confResp = restClient.get().uri(conf).retrieve().body(ConfResp.class);

        final var principalClaimValue = source.getClaimAsString(JwtClaimNames.SUB);
        if (confResp == null || confResp.userinfoEndpoint() == null || confResp.userinfoEndpoint().isEmpty()) {
            return new JwtAuthenticationToken(
                source,
                Collections.emptyList(),
                principalClaimValue);
        }
        final var userInfo = restClient.get().uri(confResp.userinfoEndpoint()).header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue)
            .retrieve().body(UserInfo.class);

        return new JwtAuthenticationToken(
            source,
            userInfo != null && userInfo.groups() != null ? userInfo.groups().stream().map(SimpleGrantedAuthority::new)
                .toList() : Collections.emptyList(),
            principalClaimValue);
    }

    public record ConfResp(@JsonProperty("userinfo_endpoint") String userinfoEndpoint) {
    }

    public record UserInfo(@JsonProperty("groups") List<String> groups) {
    }

}
