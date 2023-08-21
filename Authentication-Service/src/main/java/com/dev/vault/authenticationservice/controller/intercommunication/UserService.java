package com.dev.vault.authenticationservice.controller.intercommunication;

import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.authenticationservice.model.entity.Roles;
import com.dev.vault.authenticationservice.model.entity.User;
import com.dev.vault.authenticationservice.model.enums.Role;
import com.dev.vault.authenticationservice.repository.RolesRepository;
import com.dev.vault.authenticationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;

    public User saveUserInfo(User user) {
        return userRepository.save(user);
    }


    public Set<Roles> updateRoles(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with the provided was not found!", NOT_FOUND));
        user.getRoles().forEach(usersRoles -> usersRoles.setRole(role));

        return user.getRoles();
    }


    public Roles getProjectLeaderRole(Role role) {
        return rolesRepository.findByRole(role)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems we don't have roles with {{}} in our db 😖", role);
                    return new ResourceNotFoundException("Roles with the given roleName was not found", NOT_FOUND);
                });
    }


    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems we don't have user with {{}} in our db 😖", userId);
                    return new ResourceNotFoundException("User with the given userId was not found", NOT_FOUND);
                });
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems we don't have user with {{}} in our db 😖", email);
                    return new ResourceNotFoundException("User with the given email was not found", NOT_FOUND);
                });
    }

}
