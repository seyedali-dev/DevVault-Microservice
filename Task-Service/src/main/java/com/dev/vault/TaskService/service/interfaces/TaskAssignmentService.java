package com.dev.vault.TaskService.service.interfaces;

import com.dev.vault.TaskService.model.request.AssignTaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.shared.lib.exceptions.*;

import java.util.List;

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
    TaskResponse assignTaskToUsers(AssignTaskRequest assignTaskRequest)
            throws ResourceNotFoundException, DevVaultException, NotLeaderOfProjectException, ResourceAlreadyExistsException, NotMemberOfProjectException;


    /**
     * Assigns a task to all users in a project.
     *
     * @param taskId    The ID of the task to assign.
     * @param projectId The ID of the project to which the task belongs.
     * @return A {@link TaskResponse} containing information about the assigned task and its assigned users.
     * @throws ResourceNotFoundException   If the task or project is not found.
     * @throws NotLeaderOfProjectException If the current user is not a leader or admin of the project.
     * @throws NotMemberOfProjectException If the user is not a member of the project.
     */
    TaskResponse assignTaskToAllUsersInProject(Long taskId, Long projectId)
            throws ResourceNotFoundException, NotLeaderOfProjectException, NotMemberOfProjectException;


    void unAssignTaskFromUser(Long taskId, Long projectId, Long userId);


    void unAssignTaskFromUsers(Long taskId, Long projectId, List<Long> userIdList);


    void unassignTaskFromAllUsersInProject(Long taskId, Long projectId);

}
