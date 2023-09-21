package com.dev.vault.TaskService.service.interfaces;

import com.dev.vault.TaskService.model.request.AssignTaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.shared.lib.exceptions.*;
import com.dev.vault.shared.lib.model.response.MapResponse;

public interface TaskAssignmentService {

    /**
     * Assigns a task to a list of users.
     *
     * @param assignTaskRequest a {@link AssignTaskRequest class} containing the request data which is;
     *                          <ul>
     *                            <li> taskId -> the ID of the task to assign</li>
     *                            <li> projectId -> the Id of the project to which the task belongs </li>
     *                            <li> userIdList -> the list of user IDs to assign the task to</li>
     *                          </ul>
     * @return A {@link TaskResponse} containing information about the assigned task and its assigned users
     * @throws ResourceNotFoundException      If the task or project is not found
     * @throws DevVaultException              If the task does not belong to the project
     * @throws NotLeaderOfProjectException    If the current user is not a leader or admin of the project
     * @throws ResourceAlreadyExistsException If the task is already assigned to a user
     * @throws NotMemberOfProjectException    If the user is not a member of the project
     */
    TaskResponse assignTaskToUser_s(AssignTaskRequest assignTaskRequest)
            throws ResourceNotFoundException, DevVaultException, NotLeaderOfProjectException, ResourceAlreadyExistsException, NotMemberOfProjectException;


    /**
     * Assigns a task to all users in a project.
     *
     * @param assignTaskRequest a {@link AssignTaskRequest class} containing the request data which is;
     *                          <ul>
     *                            <li> taskId -> the ID of the task to assign</li>
     *                            <li> projectId -> the Id of the project to which the task belongs </li>
     *                          </ul>
     * @return A {@link TaskResponse} containing information about the assigned task and its assigned users.
     * @throws ResourceNotFoundException   If the task or project is not found.
     * @throws NotLeaderOfProjectException If the current user is not a leader or admin of the project.
     * @throws NotMemberOfProjectException If the user is not a member of the project.
     */
    TaskResponse assignTaskToAllUsersInProject(AssignTaskRequest assignTaskRequest)
            throws ResourceNotFoundException, NotLeaderOfProjectException, NotMemberOfProjectException;


    /**
     * Unassigns a task from a user or a list of users within a specified project.
     *
     * @param assignTaskRequest a {@link AssignTaskRequest class} containing the request data which is;
     *                          <ul>
     *                            <li> taskId -> the ID of the task to assign</li>
     *                            <li> projectId -> the Id of the project to which the task belongs </li>
     *                            <li> userIdList -> the list of user IDs to assign the task to</li>
     *                          </ul>     * @return A {@link MapResponse} object containing the message of the unassigned operation
     * @return A {@link MapResponse} object containing the message of the unassigned operation
     * @throws ResourceNotFoundException   If the specified task, project, or user is not found
     * @throws NotLeaderOfProjectException If the user attempting to unassign the task is not
     *                                     a leader of the project
     */
    MapResponse unAssignTaskFromUser_s(AssignTaskRequest assignTaskRequest)
            throws ResourceNotFoundException, NotLeaderOfProjectException;


    /**
     * Unassigns a task from all users within a specified project.
     *
     * @param assignTaskRequest a {@link AssignTaskRequest class} containing the request data which is;
     *                          <ul>
     *                            <li> taskId -> the ID of the task to assign</li>
     *                            <li> projectId -> the Id of the project to which the task belongs </li>
     *                            <li> userIdList -> the list of user IDs to assign the task to</li>
     *                          </ul>     * @return A {@link MapResponse} object containing the message of the unassigned operation
     * @return A {@link MapResponse} object containing the message of the unassigned operation
     * @throws ResourceNotFoundException   If the specified task & project is not found
     * @throws NotLeaderOfProjectException If the user attempting to unassign the task is not
     *                                     a leader or admin of the project
     */
    MapResponse unassignTaskFromAllUsersInProject(AssignTaskRequest assignTaskRequest)
            throws ResourceNotFoundException, NotLeaderOfProjectException;

}
