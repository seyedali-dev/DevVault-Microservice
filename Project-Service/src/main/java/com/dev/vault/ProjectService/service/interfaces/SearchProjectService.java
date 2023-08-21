package com.dev.vault.ProjectService.service.interfaces;

import com.dev.vault.ProjectService.exceptions.ResourceNotFoundException;
import com.dev.vault.ProjectService.model.response.SearchResponse;

import java.util.List;

public interface SearchProjectService {

    /**
     * Returns a list of all projects with their details.
     *
     * @return A list of SearchResponse objects containing project details.
     */
    List<SearchResponse> listAllProjects();


    /**
     * Searches for a project based on their name and returns their details.
     *
     * @param projectName The name of the project to search for.
     * @return A list of SearchResponse objects containing project details.
     * @throws ResourceNotFoundException If no project is found with the given name.
     */
    List<SearchResponse> searchForProject(String projectName)
            throws ResourceNotFoundException;

}
