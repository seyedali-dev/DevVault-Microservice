package com.dev.vault.authenticationservice.controller.intercommunication;

import com.dev.vault.shared.lib.exceptions.AuthenticationFailedException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.authenticationservice.model.entity.Roles;
import com.dev.vault.authenticationservice.model.entity.User;
import com.dev.vault.authenticationservice.model.enums.Role;
import com.dev.vault.authenticationservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * A controller class for intercommunicating between services.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inter-communication")
public class ServiceInterCommunicationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;


    /**
     * Retrieving the current logged-in user.
     *
     * @return the logged-in user
     * @throws AuthenticationFailedException if authentication was not successful i.e. email or password is wrong
     */
    @GetMapping("/current-user")
    public ResponseEntity<User> currentUser()
            throws AuthenticationFailedException {
        return ResponseEntity.ok(authenticationService.getCurrentUser());
    }


    /**
     * Save the user information in db.
     *
     * @param user the user to be updated
     * @return the saved user
     */
    @PostMapping("/save-user-info")
    public ResponseEntity<User> saveUserInfo(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUserInfo(user), CREATED);
    }


    /**
     * Find the {@link Role#PROJECT_LEADER PROJECT_LEADER} role.
     *
     * @param role the role to be found (specifically finding {@link Role#PROJECT_LEADER PROJECT_LEADER})
     * @return {@link Role#PROJECT_LEADER PROJECT_LEADER} -> {@link Roles} class
     * @throws ResourceNotFoundException if the role is not found
     */
    @GetMapping("/role/project-leader")
    public ResponseEntity<Roles> getProjectLeaderRole(@RequestBody Role role)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getProjectLeaderRole(role));
    }


    /**
     * Endpoint to update role for a specific user.
     *
     * @param userId The ID of the user whose role need to be updated
     * @param role   The role enum to be assigned to the user
     * @return ResponseEntity indicating the success of the update
     * @throws ResourceNotFoundException if the user is not found
     */
    @PostMapping("/{userId}/update-roles")
    public ResponseEntity<Set<Roles>> updateRoles(@PathVariable Long userId, @RequestBody Role role)
            throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.updateRoles(userId, role), CREATED);
    }


    /**
     * Find the user by the given ID.
     *
     * @param userId The ID of the user to be found
     * @return The found user
     * @throws ResourceNotFoundException if the user is not found
     */
    @GetMapping("/get-user-byId/{userId}")
    ResponseEntity<User> getUserById(@PathVariable Long userId)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getUserById(userId));
    }


    /**
     * Find the user by the given email.
     *
     * @param email The email of the user to be found
     * @return The found user
     * @throws ResourceNotFoundException if the user is not found
     */
    @GetMapping("/get-user-byEmail/{email}")
    ResponseEntity<User> getUserByEmail(@PathVariable String email)
            throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

}
