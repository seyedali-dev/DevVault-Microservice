package com.dev.vault.projectservice.controller;

import com.dev.vault.projectservice.model.request.ProjectRequest;
import com.dev.vault.projectservice.service.interfaces.ProjectManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
