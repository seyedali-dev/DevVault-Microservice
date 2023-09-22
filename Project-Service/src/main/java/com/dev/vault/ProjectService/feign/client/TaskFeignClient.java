package com.dev.vault.ProjectService.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "6-TASK-SERVICE/api/v1/task/inter-communication", configuration = FeignClientConfiguration.class)
public interface TaskFeignClient {

    @GetMapping("/find-by-assigned-users/{userId}/{taskId}")
    boolean findTaskByAssignedUser_IsUserPresent(@PathVariable long userId, @PathVariable Long taskId);

}
