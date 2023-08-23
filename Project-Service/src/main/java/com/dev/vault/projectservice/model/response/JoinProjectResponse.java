package com.dev.vault.projectservice.model.response;

import com.dev.vault.projectservice.model.enums.JoinStatus;
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
