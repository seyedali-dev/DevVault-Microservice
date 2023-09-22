package com.dev.vault.TaskService.controller.intercommunication;

import com.dev.vault.TaskService.service.intercommunication.TaskInterCommunicationService;
import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task/inter-communication")
public class TaskInterCommunicationController {

    private final TaskInterCommunicationService taskInterCommunicationService;

    @GetMapping("/get-task-by-id/{taskId}")
    public TaskDTO getTaskDTO(@PathVariable long taskId) throws ResourceNotFoundException {
        return taskInterCommunicationService.findTaskById(taskId);
    }


    @PostMapping("/validate-task-and-project/{taskId}/{projectId}/{userId}")
    public void validateTaskAndProjectAndUser(
            @PathVariable long taskId,
            @PathVariable long projectId,
            @PathVariable long userId
    ) throws DevVaultException,
            ResourceNotFoundException,
            NotMemberOfProjectException,
            NotLeaderOfProjectException {
        taskInterCommunicationService.validateTaskAndProjectAndUser(taskId, projectId, userId);
    }


}
