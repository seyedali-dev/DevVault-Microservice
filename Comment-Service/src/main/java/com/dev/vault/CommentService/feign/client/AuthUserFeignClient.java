package com.dev.vault.CommentService.feign.client;

import com.dev.vault.shared.lib.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "4-AUTHENTICATION-SERVICE/api/v1/inter-communication", configuration = FeignClientConfiguration.class)
public interface AuthUserFeignClient {

    @GetMapping("/current-user-dto")
    UserDTO getCurrentUsers_DTO(@RequestHeader("Authorization") String authHeader);


    @GetMapping("/current-user-id")
    Long getCurrentUsers_Id(@RequestHeader("Authorization") String authHeader);


    @PostMapping("/add-leader-role/{userId}")
    ResponseEntity<String> addProjectLeaderRoleToUser(@PathVariable Long userId);


    @GetMapping("/project-leader-role-id")
    Long getProjectLeaderRoleId();


    @GetMapping("/get-user/{userId}")
    UserDTO getUserDTOById(@PathVariable Long userId);

}
