package com.dev.vault.shared.lib.model.dto;

import com.dev.vault.shared.lib.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolesDTO {

    private Long roleId;
    private Role role;

}
