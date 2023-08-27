package com.dev.vault.TaskService.service.interfaces;

import com.dev.vault.TaskService.model.request.TaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.NotMemberOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;

public interface TaskManagementService {

    /**
     * Creates a new task for a given project.
     *
     * @param projectId   the ID of the project to create the task for
     * @param taskRequest the request object containing the details of the task to create
     * @return a TaskResponse object containing the details of the created task
     * @throws ResourceNotFoundException      if the project with the given ID is not found
     * @throws ResourceAlreadyExistsException if a task with the same name already exists in the project
     */
    TaskResponse createNewTask(Long projectId, TaskRequest taskRequest)
            throws ResourceNotFoundException, ResourceAlreadyExistsException;


    /**
     * Updates the details of an existing task.
     *
     * @param taskId      the ID of the task to update
     * @param taskRequest the request object containing the updated details of the task
     * @return {@link TaskResponse} object with updated task information
     * @throws ResourceNotFoundException   if the task is not found
     * @throws NotMemberOfProjectException if the requesting user is not a member of the project that the task is in
     * @throws NotLeaderOfProjectException if the requesting user is not leader or admin of the project that the task is in
     */
    TaskResponse updateTask(Long taskId, TaskRequest taskRequest)
            throws ResourceNotFoundException, NotMemberOfProjectException, NotLeaderOfProjectException;

}
