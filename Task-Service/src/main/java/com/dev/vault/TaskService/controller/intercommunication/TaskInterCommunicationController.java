package com.dev.vault.TaskService.controller.intercommunication;

import com.dev.vault.TaskService.service.intercommunication.TaskInterCommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class communicating between microservices.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task/inter-communication")
public class TaskInterCommunicationController {

    private final TaskInterCommunicationService taskInterCommunicationService;

    @GetMapping("/find-by-assigned-users/{userId}/{taskId}")
    public boolean findTaskByAssignedUser_IsUserPresent(
            @PathVariable long userId,
            @PathVariable Long taskId
    ) {
        return taskInterCommunicationService.isAlreadyAssigned_IsUserPresent(userId, taskId);
    }

}
