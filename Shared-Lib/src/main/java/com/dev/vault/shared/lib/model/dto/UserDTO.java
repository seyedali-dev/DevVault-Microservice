package com.dev.vault.shared.lib.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;

    private String username;
    private String password;
    private String email;
    private boolean active = false;
    private int age;
    private String education;
    private String major;

    private Set<RolesDTO> roles = new HashSet<>();

}
