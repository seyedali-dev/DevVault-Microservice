package com.dev.vault.authenticationservice.controller.intercommunication;

import com.dev.vault.authenticationservice.service.intercommunication.UserInterCommunicationService;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import com.dev.vault.shared.lib.model.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * A controller class for intercommunicating between services.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inter-communication")
public class InterCommunicationController {

    private final UserInterCommunicationService userInterCommunicationService;


    /**
     * Returns the current logged-in user mapped as {@link UserDTO}.
     *
     * @param authHeader authentication header i.e. the JWT token
     * @return the logged-in user mapped as {@link UserDTO}
     */
    @GetMapping("/current-user-dto")
    public UserDTO getCurrentUsers_DTO(@RequestHeader("Authorization") String authHeader) {
        return userInterCommunicationService.getCurrentUserAsDTO(authHeader);
    }


    /**
     * Returns the current logged-in user's ID.
     *
     * @param authHeader authentication header i.e. the JWT token
     * @return the logged-in user's ID
     */
    @GetMapping("/current-user-id")
    public Long getCurrentUsers_Id(@RequestHeader("Authorization") String authHeader) {
        return userInterCommunicationService.getCurrentUserAsDTO(authHeader).getUserId();
    }


    /**
     * Adds the {@link Role#PROJECT_LEADER} to the specified user i.e.
     * by its ID.
     *
     * @param userId the id of the user that we want to add the role to
     * @return a String message
     * @throws ResourceNotFoundException if the {@link Role#PROJECT_LEADER} was not found
     */
    @PostMapping("/add-leader-role/{userId}")
    @ResponseStatus(CREATED)
    public ResponseEntity<String> addProjectLeaderRoleToUser(@PathVariable Long userId) {
        userInterCommunicationService.add_ProjectLeaderRole(userId);
        return new ResponseEntity<>("PROJECT_LEADER role added to user", CREATED);
    }


    /**
     * Get the ID of the {@link Role#PROJECT_LEADER} role.
     *
     * @return the ID of the found {@link Role#PROJECT_LEADER}
     * @throws ResourceNotFoundException if the {@link Role#PROJECT_LEADER} was not found
     */
    @GetMapping("/project-leader-role-id")
    public Long getProjectLeaderRoleId() {
        return userInterCommunicationService.getProjectLeaderRole().getRoleId();
    }


    /**
     * Find a user by its id, and then map it to the {@link UserDTO}.
     *
     * @param userId the id of the user that we are looking for
     * @return the found user mapped as {@link UserDTO}
     * @throws ResourceNotFoundException if the user is not found
     */
    @GetMapping("/get-user/{userId}")
    public UserDTO getUserDTOById(@PathVariable Long userId) {
        return userInterCommunicationService.getUserDTOById(userId);
    }

}
