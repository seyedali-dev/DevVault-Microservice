package com.dev.vault.ProjectService.controller;

import com.dev.vault.ProjectService.model.dto.ProjectMembersDto;
import com.dev.vault.ProjectService.model.request.ProjectRequest;
import com.dev.vault.ProjectService.service.interfaces.ProjectManagementService;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * REST controller for managing projects.
 */
@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectManagementController {

    private final ProjectManagementService projectManagementService;

    /**
     * Endpoint for creating a new project.
     *
     * @param projectRequest the project details to create
     * @return a ResponseEntity containing the created project and an HTTP status code of 201 (CREATED)
     */
    @PostMapping("/create-project")
    public ResponseEntity<ProjectRequest> createProject(@RequestBody ProjectRequest projectRequest) {
        return new ResponseEntity<>(projectManagementService.createProject(projectRequest), CREATED);
    }


    /**
     * Lists all the members of the specified project.
     *
     * @param projectId the members of the project that we want to see
     * @return list of project members as {@link ProjectMembersDto}
     * @throws ResourceNotFoundException if the project was not found
     */
    @GetMapping("/list-members/{projectId}")
    public ResponseEntity<ProjectMembersDto> listProjectMembers(
            @PathVariable Long projectId
    ) throws ResourceNotFoundException {
        return ResponseEntity.ok(projectManagementService.listMembersOfProject(projectId));
    }

}
