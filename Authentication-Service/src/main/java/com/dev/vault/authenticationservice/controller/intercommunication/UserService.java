package com.dev.vault.authenticationservice.controller.intercommunication;

import com.dev.vault.authenticationservice.model.entity.Roles;
import com.dev.vault.authenticationservice.model.entity.User;
import com.dev.vault.authenticationservice.repository.RolesRepository;
import com.dev.vault.authenticationservice.repository.UserRepository;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.RolesDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.dev.vault.shared.lib.model.enums.Role.PROJECT_LEADER;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;

    public void add_ProjectLeaderRole(Long userId) {
        Roles projectLeaderRole = getProjectLeaderRole();
        User user = getUserById(userId);

        Set<Roles> roles = user.getRoles();

        for (Roles role : roles) {
            if (!role.getRole().equals(PROJECT_LEADER)) {
                user.getRoles().add(projectLeaderRole);
                userRepository.save(user);
            } else {
                log.warn("user already has this role - skipping the adding new role process");
                return;
            }
        }
    }


    public Roles getProjectLeaderRole() {
        return rolesRepository.findByRole(PROJECT_LEADER)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems we don't have roles with {{}} in our db 😖", PROJECT_LEADER);
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


    public UserDTO getUserDTOById(Long userId) {
        User user = getUserById(userId);

        Set<RolesDTO> rolesDTO = new HashSet<>();

        for (Roles roles : user.getRoles()) {
            RolesDTO buildRolesDTO = RolesDTO.builder()
                    .roleId(roles.getRoleId())
                    .role(roles.getRole())
                    .build();

            rolesDTO.add(buildRolesDTO);
        }

        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .age(user.getAge())
                .active(user.isActive())
                .major(user.getMajor())
                .education(user.getEducation())
                .roles(rolesDTO)
                .build();
    }

}
