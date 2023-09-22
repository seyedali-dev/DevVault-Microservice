package com.dev.vault.CommentService.feign.client;

import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.TaskDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "6-TASK-SERVICE/api/v1/task/inter-communication", configuration = FeignClientConfiguration.class)
public interface TaskUtilFeignClient {

    @GetMapping("/get-task-by-id/{taskId}")
    TaskDTO getTaskDTO(@PathVariable long taskId) throws ResourceNotFoundException;


    @PostMapping("/validate-task-and-project/{taskId}/{projectId}/{userId}")
    void validateTaskAndProjectAndUser(
            @PathVariable long taskId,
            @PathVariable long projectId,
            @PathVariable long userId
    ) throws DevVaultException,
            ResourceNotFoundException,
            NotMemberOfProjectException,
            NotLeaderOfProjectException;

}
