package com.dev.vault.ProjectService.model.response;

import com.dev.vault.ProjectService.model.enums.JoinStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinProjectResponse {

    private String projectName;
    private Long joinRequestId;
    private String joinRequestUsersEmail;
    private JoinStatus joinStatus;

}
