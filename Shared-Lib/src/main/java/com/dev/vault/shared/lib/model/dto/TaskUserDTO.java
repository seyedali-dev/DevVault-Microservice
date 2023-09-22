package com.dev.vault.shared.lib.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskUserDTO {

    private Long taskUserId;

    /* relationships */
    private TaskDTO taskDTO;
    private long userId;
    /* end of relationships */

}
