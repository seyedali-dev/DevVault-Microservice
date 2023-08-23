package com.dev.vault.projectservice.service.interfaces;

import com.dev.vault.projectservice.model.request.ProjectRequest;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;

public interface ProjectManagementService {

    /**
     * Creates a new project with the provided {@link ProjectRequest} class.
     *
     * @param projectRequest the projectRequest containing the project information like name and description
     * @return the same {@link ProjectRequest} class with the newly created project's information
     * @throws ResourceAlreadyExistsException if a project with the same name already exists
     * @throws ResourceNotFoundException      if the PROJECT_LEADER role is not found
     */
    ProjectRequest createProject(ProjectRequest projectRequest)
            throws ResourceNotFoundException, ResourceAlreadyExistsException;

}
