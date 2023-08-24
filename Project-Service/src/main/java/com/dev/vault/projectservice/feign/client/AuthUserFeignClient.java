package com.dev.vault.projectservice.feign.client;

import com.dev.vault.shared.lib.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AUTHENTICATION-SERVICE/api/v1/inter-communication", configuration = FeignClientConfiguration.class)
public interface AuthUserFeignClient {

    @GetMapping("/current-user-id")
    Long getCurrentUserId(@RequestHeader("Authorization") String authHeader);


    @PostMapping("/add-leader-role/{userId}")
    ResponseEntity<String> addProjectLeaderRoleToUser(@PathVariable Long userId);


    @GetMapping("/project-leader-role-id")
    Long getProjectLeaderRoleId();


    @GetMapping("/get-username/{userId}")
    String getUserNameById(@PathVariable Long userId);


    @GetMapping("/get-user/{userId}")
    UserDTO getUserDTO(@PathVariable Long userId);

}
