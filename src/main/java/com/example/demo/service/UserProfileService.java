package com.example.demo.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserProfileResource;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.representations.userprofile.config.UPAttribute;
import org.keycloak.representations.userprofile.config.UPAttributePermissions;
import org.keycloak.representations.userprofile.config.UPConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserProfileService {

    public void updateProfile() {
        try (Keycloak keycloak = KeycloakBuilder.builder().serverUrl("").build()) {


            UserProfileResource userProfileResource = keycloak.realm("realm").users().userProfile();

            UPConfig configuration = userProfileResource.getConfiguration();

            configuration.setAttributes(List.of(
                new UPAttribute("email", new UPAttributePermissions(Set.of("admin", "user"), Set.of("admin", "user"))),
                new UPAttribute("firstName", new UPAttributePermissions(Set.of("admin", "user"), Set.of("admin", "user")))
            ));

            userProfileResource.update(configuration);
        }
    }
}
