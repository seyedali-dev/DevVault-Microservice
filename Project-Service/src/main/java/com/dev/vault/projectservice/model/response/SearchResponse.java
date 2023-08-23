package com.dev.vault.projectservice.model.response;

import com.dev.vault.projectservice.model.dto.ProjectMembersDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResponse {
    private Long projectId;
    private String projectName;
    private String leaderName;
    private String projectDescription;
    private ProjectMembersDto members;
}
