package com.dev.vault.ProjectService.feign.client;

import com.dev.vault.ProjectService.model.dto.Role;
import com.dev.vault.ProjectService.model.dto.Roles;
import com.dev.vault.ProjectService.model.dto.User;
import com.dev.vault.shared.lib.exceptions.AuthenticationFailedException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(name = "AUTHENTICATION-SERVICE/api/v1/inter-communication", configuration = FeignClientConfiguration.class)
public interface AuthFeignClient {

    @GetMapping("/current-user")
    ResponseEntity<User> getCurrentUser()
            throws AuthenticationFailedException;


    @GetMapping("/role/project-leader")
    ResponseEntity<Roles> getProjectLeaderRole(@RequestBody Role role)
            throws ResourceNotFoundException;


    @PostMapping("/{userId}/update-roles")
    ResponseEntity<Set<Roles>> updateRoles(@PathVariable Long userId, @RequestBody Role role)
            throws ResourceNotFoundException;


    @GetMapping("/get-user-byId/{userId}")
    ResponseEntity<User> getUserById(@PathVariable Long userId)
            throws ResourceNotFoundException;


    @PostMapping("/save-user-info")
    ResponseEntity<User> saveUserInfo(@RequestBody User user);


    @GetMapping("/get-user-byEmail/{email}")
    ResponseEntity<User> getUserByEmail(@PathVariable String email);

}
