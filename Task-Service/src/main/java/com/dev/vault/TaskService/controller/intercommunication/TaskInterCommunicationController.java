package com.dev.vault.TaskService.controller.intercommunication;

import com.dev.vault.TaskService.service.intercommunication.TaskInterCommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task/inter-communication")
public class TaskInterCommunicationController {

    private final TaskInterCommunicationService taskInterCommunicationService;

    /**
     * Validate whether task exists in the db.
     *
     * @param taskId the ID of the task to find
     * @return boolean of either ture if the task is present, otherwise false
     */
    @GetMapping("/validate-task-existence-by-id/{taskId}")
    public boolean validateTaskExistence(@PathVariable long taskId) {
        return taskInterCommunicationService.validateTaskExistence(taskId);
    }

}
