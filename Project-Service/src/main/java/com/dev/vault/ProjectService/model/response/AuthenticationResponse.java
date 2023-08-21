package com.dev.vault.ProjectService.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
