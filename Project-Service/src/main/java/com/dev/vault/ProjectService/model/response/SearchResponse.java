package com.dev.vault.ProjectService.model.response;

import com.dev.vault.ProjectService.model.dto.ProjectMembersDto;
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
