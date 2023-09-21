package com.dev.vault.TaskService.controller;

import com.dev.vault.TaskService.model.request.AssignTaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.TaskService.service.interfaces.TaskAssignmentService;
import com.dev.vault.shared.lib.exceptions.*;
import com.dev.vault.shared.lib.model.enums.Role;
import com.dev.vault.shared.lib.model.response.MapResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task/assignment")
@RequiredArgsConstructor
public class TaskAssignmentController {

    private final TaskAssignmentService taskService;

    /**
     * Assigns a task to a list of users.
     *
     * @param assignTaskRequest a {@link AssignTaskRequest class} containing the request data which is;
     *                          <ul>
     *                            <li> taskId -> the ID of the task to assign</li>
     *                            <li> projectId -> the Id of the project to which the task belongs </li>
     *                            <li> userIdList -> the list of user IDs to assign the task to</li>
     *                          </ul>
     * @return a ResponseEntity with an OK HTTP status code and a map of responses for each assigned user
     * @throws ResourceNotFoundException      If the task or project is not found
     * @throws DevVaultException              If the task does not belong to the project
     * @throws NotLeaderOfProjectException    If the current user is not a leader or admin of the project
     * @throws ResourceAlreadyExistsException If the task is already assigned to a user
     * @throws NotMemberOfProjectException    If the user is not a member of the project
     */
    @PostMapping("/assignTaskToUsers")
    public ResponseEntity<TaskResponse> assignTaskToUsers(
            @RequestBody AssignTaskRequest assignTaskRequest
    ) throws
            ResourceNotFoundException, DevVaultException,
            NotLeaderOfProjectException, ResourceAlreadyExistsException,
            NotMemberOfProjectException {
        return ResponseEntity.ok(taskService.assignTaskToUsers(assignTaskRequest));
    }


    /**
     * Assigns a task to all users in a project.
     *
     * @param assignTaskRequest a {@link AssignTaskRequest class} containing the request data which is;
     *                          <ul>
     *                            <li> taskId -> the ID of the task to assign</li>
     *                            <li> projectId -> the Id of the project to which the task belongs </li>
     *                          </ul>
     * @return a ResponseEntity containing a TaskResponse object and an HTTP status code
     * @throws ResourceNotFoundException   If the task or project is not found.
     * @throws NotLeaderOfProjectException If the current user is not a leader or admin of the project.
     * @throws NotMemberOfProjectException If the user is not a member of the project.
     */
    @PostMapping("/assignTask/all")
    public ResponseEntity<TaskResponse> assignTaskToAllUserInProject(
            @RequestBody AssignTaskRequest assignTaskRequest
    ) throws ResourceNotFoundException, NotLeaderOfProjectException, NotMemberOfProjectException {
        return ResponseEntity.ok(taskService.assignTaskToAllUsersInProject(assignTaskRequest));
    }


    /**
     * Unassigns a task from a specific user or a list of users.
     *
     * @param assignTaskRequest a {@link AssignTaskRequest class} containing the request data which is;
     *                          <ul>
     *                            <li> taskId -> the ID of the task to assign</li>
     *                            <li> projectId -> the Id of the project to which the task belongs </li>
     *                            <li> userIdList -> the list of user IDs to assign the task to</li>
     *                          </ul>
     * @return A {@link MapResponse} object containing the message of the unassigned operation
     * @throws ResourceNotFoundException   If the specified task, project, or user is not found
     * @throws NotLeaderOfProjectException If the user attempting to unassign the task is not
     *                                     a {@link Role#PROJECT_LEADER leader} or {@link Role#PROJECT_ADMIN admin} of the project
     */
    @DeleteMapping("/unassignTask")
    public ResponseEntity<MapResponse> unassignTaskFromUsers(
            @RequestBody AssignTaskRequest assignTaskRequest
    ) throws ResourceNotFoundException, NotLeaderOfProjectException {
        return ResponseEntity.ok(taskService.unAssignTaskFromUsers(assignTaskRequest));
    }

//    /**
//     * Unassigns a task from a all users in a project.
//     *
//     * @param taskId    the ID of the task to unassign
//     * @param projectId the ID of the project the task belongs to
//     * @return a ResponseEntity with an OK HTTP status code
//     */
//    @DeleteMapping("/unassignTask/all") //TODO
//    public ResponseEntity<Void> unassignTaskFromAllUsersInProject(
//            @RequestParam("taskId") Long taskId,
//            @RequestParam("projectId") Long projectId
//    ) {
////        taskService.unassignTaskFromUser(taskId, projectId, userId);
////        return ResponseEntity.ok().build();
//        return null;
//    }

}
