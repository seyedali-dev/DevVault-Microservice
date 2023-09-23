package com.dev.vault.CommentService.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "6-TASK-SERVICE/api/v1/task/inter-communication", configuration = FeignClientConfiguration.class)
public interface TaskUtilFeignClient {

    @GetMapping("/validate-task-existence-by-id/{taskId}")
    boolean validateTaskExistence(@PathVariable long taskId);

}
