package com.dev.vault.ProjectService.service;

import com.dev.vault.ProjectService.feign.client.AuthFeignClient;
import com.dev.vault.ProjectService.model.dto.Role;
import com.dev.vault.ProjectService.model.dto.Roles;
import com.dev.vault.ProjectService.model.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetrieveDataService {

    private final AuthFeignClient authFeignClient;

    public User get_CurrentUser_FromAuthenticationService() {
        return authFeignClient.getCurrentUser().getBody();
    }


    public User get_UserById_FromAuthenticationService(Long userId) {
        return authFeignClient.getUserById(userId).getBody();
    }


    public Roles get_ProjectLeaderRole_FromAuthenticationService(Role role) {
        return authFeignClient.getProjectLeaderRole(role).getBody();
    }


    public User get_UserByEmail_FromAuthenticationService(String email) {
        return authFeignClient.getUserByEmail(email).getBody();
    }


    public void save_UserInfo_ForAuthenticationService(User currentUser) {
        authFeignClient.saveUserInfo(currentUser);
    }

}
