package com.dev.vault.TaskService.controller;

import com.dev.vault.TaskService.model.enums.TaskPriority;
import com.dev.vault.TaskService.model.enums.TaskStatus;
import com.dev.vault.TaskService.model.request.TaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.TaskService.service.interfaces.TaskManagementService;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for task management such as; new task, delete task, update task, search task, get details of task, export tasks.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task/management")
public class TaskManagementController {

    private final TaskManagementService taskService;

    /**
     * Creates a new task for the specified project.
     *
     * @param projectId   the ID of the project that the task is being created for
     * @param taskRequest the request object containing the details of the task to create
     * @return a ResponseEntity containing a TaskResponse object and an HTTP status code
     */
    @PostMapping("/new-task/{projectId}")
    public ResponseEntity<TaskResponse> newTask(
            @Valid @PathVariable Long projectId,
            @RequestBody TaskRequest taskRequest
    ) throws ResourceNotFoundException, ResourceAlreadyExistsException {
        return new ResponseEntity<>(taskService.createNewTask(projectId, taskRequest), HttpStatus.CREATED);
    }


    /**
     * Updates the details of an existing task.
     *
     * @param taskId      the ID of the task to update
     * @param taskRequest the request object containing the updated details of the task
     * @return a ResponseEntity containing a TaskResponse object and an HTTP status code
     * @throws ResourceNotFoundException   if the task is not found
     * @throws NotMemberOfProjectException if the requesting user is not a member of the project that the task is in
     * @throws NotLeaderOfProjectException if the requesting user is not leader or admin of the project that the task is in
     */
    @PutMapping("/update-task/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @Valid @PathVariable Long taskId,
            @RequestBody TaskRequest taskRequest
    ) throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskRequest));
    }


    /**
     * Retrieves the details of a specific task by its ID.
     *
     * @param taskId the ID of the task to retrieve
     * @return a ResponseEntity containing a TaskResponse object and an HTTP status code
     * @throws ResourceNotFoundException if the task is not found
     */
    @GetMapping("/task/{taskId}")
    public ResponseEntity<TaskResponse> getTaskDetails(
            @PathVariable Long taskId
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok(taskService.getTaskDetails(taskId));
    }


    /**
     * Deletes a task by its ID.
     *
     * @param taskId the ID of the task to delete
     * @return a ResponseEntity with an OK HTTP status code
     */
    @DeleteMapping("/deleteTask/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    } // TODO: only member and leader of project can delete a task


    /**
     * Searches for tasks based on different criteria.
     *
     * @param status            the status of the tasks to search for
     * @param priority          the priority of the tasks to search for
     * @param projectId         the ID of the project to search for tasks in
     * @param assignedTo_UserId the ID of the user the tasks are assigned to
     * @return a ResponseEntity containing a list of TaskResponse objects and an HTTP status code
     */
    @GetMapping("/searchTasks")
    public ResponseEntity<List<TaskResponse>> searchTasks(
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "priority", required = false) TaskPriority priority,
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "assignedTo_UserId", required = false) Long assignedTo_UserId
    ) {
        return ResponseEntity.ok(taskService.searchTaskBasedOnDifferentCriteria(status, priority, projectId, assignedTo_UserId));
    } // TODO: query did not return a unique result: 2


    /**
     * Exports tasks to a specified format.
     *
     * @param format     the format to export the tasks to (CSV, Excel, PDF, etc.)
     * @param status     the status of the tasks to export
     * @param priority   the priority of the tasks to export
     * @param projectId  the ID of the project to export tasks for
     * @param assignedTo the ID of the user the tasks are assigned to
     * @return a ResponseEntity containing the exported file and an HTTP status code
     */
    @GetMapping("/exportTasks") //TODO
    public ResponseEntity<?> exportTasks(
            @RequestParam(value = "format") String format,
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "priority", required = false) TaskPriority priority,
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "assignedTo", required = false) Long assignedTo
    ) {
        return null;
    }

}