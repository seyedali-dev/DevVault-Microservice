package com.dev.vault.TaskService.service.interfaces;

import com.dev.vault.TaskService.model.request.TaskRequest;
import com.dev.vault.TaskService.model.response.TaskResponse;
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

}
