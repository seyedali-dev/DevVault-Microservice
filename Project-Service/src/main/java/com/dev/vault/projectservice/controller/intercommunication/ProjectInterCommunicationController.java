package com.dev.vault.ProjectService.controller.intercommunication;

import com.dev.vault.ProjectService.service.intercommunication.ProjectInterCommunicationService;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import com.dev.vault.shared.lib.model.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller class for intercommunicating between services.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/inter-communication")
public class ProjectInterCommunicationController {

    private final ProjectInterCommunicationService interCommunicationService;

    /**
     * Checks whether the provided user is a member of the project.
     *
     * @param projectId the project
     * @param userId    the user
     * @return a boolean indicating true if the user is member or not
     * @throws ResourceNotFoundException if the project or user is not found
     */
    @GetMapping("/is-member-of-project")
    public boolean isMemberOfProject(@RequestParam long projectId, @RequestParam long userId)
            throws ResourceNotFoundException {
        return interCommunicationService.isMemberOfProject(projectId, userId);
    }


    /**
     * Checks whether the provided user is a {@link Role#PROJECT_LEADER} or {@link Role#PROJECT_ADMIN}.
     *
     * @param projectId the project
     * @param userId    the user
     * @return a boolean indicating true if the user is leader/admin
     * @throws ResourceNotFoundException if the project or user is not found
     */
    @GetMapping("/is-leader-of-project")
    boolean isLeaderOrAdminOfProject(@RequestParam long projectId, @RequestParam long userId)
            throws ResourceNotFoundException {
        return interCommunicationService.isLeaderOrAdminOfProject(projectId, userId);
    }


    /**
     * Find a project by its ID and then return it as a {@link ProjectDTO}.
     *
     * @param projectId the ID of the project
     * @return the found project as {@link ProjectDTO}
     * @throws ResourceNotFoundException if the project is not found.
     */
    @GetMapping("/project-dto/{projectId}")
    public ProjectDTO getProjectAsDTO(@PathVariable long projectId)
            throws ResourceNotFoundException {
        return interCommunicationService.getProjectDTO(projectId);
    }


    /**
     * Retrieves a set of users associated with a task and a project.
     *
     * @param projectId The project to which the task belongs.
     * @return A set of users associated with the task and the project.
     */
    @GetMapping("/get-user-association-with-task-and-project/{projectId}")
    public List<UserDTO> getUsersAssociatedWithTaskAndProject(@PathVariable long projectId) {
        return interCommunicationService.getUsersAssociatedWithTaskAndProject(projectId);
    }

}
