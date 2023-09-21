package com.dev.vault.TaskService.controller;

import com.dev.vault.TaskService.model.request.AssignTaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.TaskService.service.interfaces.TaskAssignmentService;
import com.dev.vault.shared.lib.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> assignTaskToAllUserInProject(
            @RequestBody AssignTaskRequest assignTaskRequest
    ) throws ResourceNotFoundException, NotLeaderOfProjectException, NotMemberOfProjectException {
        return ResponseEntity.ok(taskService.assignTaskToAllUsersInProject(assignTaskRequest));
    }

//    /**
//     * Unassigns a task from a specific user.
//     *
//     * @param taskId    the ID of the task to unassign
//     * @param projectId the ID of the project the task belongs to
//     * @param userId    the ID of the user to unassign the task from
//     * @return a ResponseEntity with an OK HTTP status code
//     */
//    @DeleteMapping("/unassignTask") //TODO
//    public ResponseEntity<Void> unassignTaskFromUser(
//            @RequestParam("taskId") Long taskId,
//            @RequestParam("projectId") Long projectId,
//            @RequestParam("userId") Long userId
//    ) {
////        taskService.unassignTaskFromUser(taskId, projectId, userId);
////        return ResponseEntity.ok().build();
//        return null;
//    }
//
//    /**
//     * Unassigns a task from a list of users.
//     *
//     * @param taskId    the ID of the task to unassign
//     * @param projectId the ID of the project the task belongs to
//     * @param userIdList    the IDs of the users to unassign the task from
//     * @return a ResponseEntity with an OK HTTP status code
//     */
//    @DeleteMapping("/unassignTask/multiple/users") //TODO
//    public ResponseEntity<Void> unassignTaskFromUsers(
//            @RequestParam("taskId") Long taskId,
//            @RequestParam("projectId") Long projectId,
//            @RequestBody List<Long> userIdList
//    ) {
////        taskService.unassignTaskFromUser(taskId, projectId, userId);
////        return ResponseEntity.ok().build();
//        return null;
//    }
//
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
