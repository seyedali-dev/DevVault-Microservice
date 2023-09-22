package com.dev.vault.TaskService.controller;

import com.dev.vault.TaskService.service.interfaces.TaskProgressService;
import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.enums.TaskStatus;
import com.dev.vault.shared.lib.model.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * REST controller for marking tasks as completed.
 * Requires the `PROJECT_LEADER` or `PROJECT_ADMIN` role to access.
 */
@RestController
@RequestMapping("/api/v1/task/progress")
@RequiredArgsConstructor
public class TaskProgressController {

    private final TaskProgressService taskService;

    /**
     * Marks a task as completed.
     *
     * @param taskId     the ID of the task to mark as completed (passed as a request parameter).
     * @param projectId  the ID of the project that the task belongs to (passed as a request parameter).
     * @param taskStatus the new status of the task (passed as a request parameter).
     * @return a ResponseEntity with a status of 200 OK if the task was successfully marked as completed.
     * @throws DevVaultException           if the task has already been completed, or if the task status is not {@link TaskStatus#COMPLETED}.
     * @throws ResourceNotFoundException   if the task or project with the given IDs cannot be found in the database.
     * @throws NotMemberOfProjectException if the current user is not a member of the project with the given ID.
     * @throws NotLeaderOfProjectException if the current user is not a leader or admin of the project with the given ID.
     */
    @PostMapping("/{taskId}/{projectId}")
    public ResponseEntity<ApiResponse> markTaskAsCompleted(
            @PathVariable("taskId") Long taskId,
            @PathVariable("projectId") Long projectId,
            @RequestParam TaskStatus taskStatus
    ) throws DevVaultException, ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());

        taskService.markTaskAsCompleted(taskId, projectId, taskStatus);

        return ResponseEntity.ok(new ApiResponse(
                        "Congratulations on completing the Task. Task Completed at: " + formattedDate + "!",
                        true
                )
        );
    }

}
