package com.dev.vault.TaskService.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignTaskRequest {

    private long taskId;
    private long projectId;
    private List<Long> userIdList;

}
