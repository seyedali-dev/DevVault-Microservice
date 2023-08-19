package com.dev.vault.authenticationservice.model.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String username;
    private List<String> roles;
    private List<String> rolesDescription;
    private String token;

}
