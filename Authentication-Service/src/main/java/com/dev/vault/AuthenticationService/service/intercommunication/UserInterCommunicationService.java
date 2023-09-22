package com.dev.vault.AuthenticationService.service.intercommunication;

import com.dev.vault.AuthenticationService.config.jwt.JwtService;
import com.dev.vault.AuthenticationService.model.entity.Roles;
import com.dev.vault.AuthenticationService.model.entity.User;
import com.dev.vault.AuthenticationService.repository.RolesRepository;
import com.dev.vault.AuthenticationService.repository.UserRepository;
import com.dev.vault.shared.lib.exceptions.AuthenticationFailedException;
import com.dev.vault.shared.lib.exceptions.MissingAuthenticationHeaderException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.dto.RolesDTO;
import com.dev.vault.shared.lib.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.dev.vault.shared.lib.model.enums.Role.PROJECT_LEADER;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInterCommunicationService {

    @Value("${token.prefix}")
    private String TOKEN_PREFIX;

    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public void add_ProjectLeaderRole(Long userId) {
        Roles projectLeaderRole = get_ProjectLeaderRole();
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


    public Roles get_ProjectLeaderRole() {
        return rolesRepository.findByRole(PROJECT_LEADER)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems we don't have roles with {{}} in our db 😖", PROJECT_LEADER);
                    return new ResourceNotFoundException("Roles with the given roleName was not found", NOT_FOUND, NOT_FOUND.value());
                });
    }


    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems we don't have user with {{}} in our db 😖", userId);
                    return new ResourceNotFoundException(
                            "😖 User with the given userId was NOT found 😖",
                            NOT_FOUND,
                            NOT_FOUND.value()
                    );
                });
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("😖 huh... it seems we don't have user with {{}} in our db 😖", email);
                    return new ResourceNotFoundException("User with the given email was not found", NOT_FOUND, NOT_FOUND.value());
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


    public UserDTO getCurrentUserAsDTO(String authHeader) {
        if (authHeader == null) {
            log.error("🔐 Authentication header is missing 🔐");
            throw new MissingAuthenticationHeaderException("auth header is missing", UNAUTHORIZED);
        }

        if (!authHeader.startsWith(TOKEN_PREFIX)) {
            log.error("⭕ Provided invalid authentication header ⭕");
            throw new AuthenticationFailedException("Authentication header is not valid", UNAUTHORIZED);
        }

        String token = authHeader.substring(TOKEN_PREFIX.length());
        String email = jwtService.extractUsername(token);
        User user = getUserByEmail(email);

        return getUserDTOById(user.getUserId());
    }

}
