package com.dev.vault.projectservice.feign.client;

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
    ResponseEntity<Void> addProjectLeaderRoleToUser(@PathVariable Long userId);


    @GetMapping("/project-leader-role-id")
    Long getProjectLeaderRoleId();

}
