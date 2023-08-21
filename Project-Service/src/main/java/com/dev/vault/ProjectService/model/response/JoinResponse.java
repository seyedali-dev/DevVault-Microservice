package com.dev.vault.ProjectService.model.response;

import com.dev.vault.ProjectService.model.enums.JoinStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinResponse {

    private String status;
    private JoinStatus joinStatus;

}
