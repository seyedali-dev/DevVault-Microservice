package com.dev.vault.projectservice.controller.intercommunication;

import com.dev.vault.shared.lib.exceptions.NotLeaderOfProjectException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.ProjectDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * A controller class for intercommunicating between services.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/inter-communication")
public class InterCommunicationController {

    private final ProjectInterCommunicationService interCommunicationService;

    @GetMapping("/is-member-of-project")
    public boolean isMemberOfProject(@RequestParam long projectId, @RequestParam long userId)
            throws ResourceNotFoundException {
        return interCommunicationService.isMemberOfProject(projectId, userId);
    }


    @GetMapping("/is-leader-of-project")
    boolean isLeaderOrAdminOfProject(@RequestParam long projectId, @RequestParam long userId)
            throws NotLeaderOfProjectException, ResourceNotFoundException {
        return interCommunicationService.isLeaderOrAdminOfProject(projectId, userId);
    }


    @GetMapping("/project-dto/{projectId}")
    public ProjectDTO getProjectAsDTO(@PathVariable long projectId)
            throws ResourceNotFoundException {
        return interCommunicationService.getProjectDTO(projectId);
    }

}
