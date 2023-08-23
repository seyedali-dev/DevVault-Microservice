package com.dev.vault.authenticationservice.controller.intercommunication;

import com.dev.vault.authenticationservice.config.jwt.JwtService;
import com.dev.vault.authenticationservice.service.AuthenticationService;
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
public class ServiceInterCommunicationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtService jwtService;


    @GetMapping("/current-user-id")
    public Long getCurrentUserId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token);
        return userService.getUserByEmail(email).getUserId();
//        return authenticationService.getCurrentUser();
    }


    @PostMapping("/add-leader-role/{userId}")
    @ResponseStatus(CREATED)
    public ResponseEntity<Void> addProjectLeaderRoleToUser(@PathVariable Long userId) {
        userService.add_ProjectLeaderRole(userId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/project-leader-role-id")
    public Long getProjectLeaderRoleId() {
        return userService.getProjectLeaderRole().getRoleId();
    }

}
