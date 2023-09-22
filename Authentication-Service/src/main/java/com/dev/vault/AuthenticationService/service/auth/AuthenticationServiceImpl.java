package com.dev.vault.AuthenticationService.service.auth;

import com.dev.vault.AuthenticationService.config.jwt.JwtService;
import com.dev.vault.AuthenticationService.model.entity.Roles;
import com.dev.vault.AuthenticationService.model.entity.User;
import com.dev.vault.AuthenticationService.model.entity.VerificationToken;
import com.dev.vault.AuthenticationService.model.request.AuthenticationRequest;
import com.dev.vault.AuthenticationService.model.request.RegisterRequest;
import com.dev.vault.AuthenticationService.repository.RolesRepository;
import com.dev.vault.AuthenticationService.repository.UserRepository;
import com.dev.vault.AuthenticationService.repository.VerificationTokenRepository;
import com.dev.vault.AuthenticationService.util.AuthenticationUtils;
import com.dev.vault.shared.lib.exceptions.AuthenticationFailedException;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.shared.lib.model.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dev.vault.shared.lib.model.enums.Role.TEAM_MEMBER;
import static org.springframework.http.HttpStatus.*;

/**
 * Authentication implementation: Registration & Login.
 */
@SuppressWarnings("unused")
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${account.verification.auth.url}")
    private String ACCOUNT_VERIFICATION_AUTH_URL;

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    //    private final MailService mailService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final AuthenticationUtils authenticationUtils;

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthenticationResponse registerUser(RegisterRequest registerRequest) {
        // check if user already exists in the database
        Optional<User> foundUser = userRepository.findByEmail(registerRequest.getEmail());

        if (foundUser.isPresent()) {
            log.info("❌ This user already exists! provide unique email. ❌");
            throw new ResourceAlreadyExistsException(
                    "⭕ User with email {" + registerRequest.getEmail() + "} already exists.. provide a unique email ⭕",
                    BAD_REQUEST,
                    BAD_REQUEST.value()
            );
        }

        // find the TEAM_MEMBER role and assign it to newly created user as default role
        Roles teamMemberRole = rolesRepository.findByRole(TEAM_MEMBER)
                .orElseThrow(() -> new ResourceNotFoundException("Role with TEAM_MEMBER role was not found", NOT_FOUND, NOT_FOUND.value()));

        // create a new user object and map the properties from the register request
        User user = modelMapper.map(registerRequest, User.class);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.getRoles().add(teamMemberRole);

        // save the user object to the database
        User savedUser = userRepository.save(user);
        log.info("✅ User saved to db, attempting to send activation email...");

        // generate a verification token and send an email with the activation link  // TODO:: implement account verification
        String verificationToken = authenticationUtils.generateVerificationToken(user);
//        mailService.sendEmail(new Email(
//                "Please Activate Your Account",
//                user.getEmail(),
//                "Thank you for signing up to our app! " +
//                "Please click the url below to activate your account: " + ACCOUNT_VERIFICATION_AUTH_URL + verificationToken));

        log.info("➡️ generating JWT token...");
        // generate and return a JWT token for the newly created user
        String jwtToken = jwtService.generateToken(user);

        // save the generated token
        authenticationUtils.buildAndSaveJwtToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .roles(user.getRoles()
                        .stream().map(roles -> roles.getRole().name())
                        .toList()
                )
                .rolesDescription(List.of("➡️➡️Default role for user is TEAM_MEMBER"))
                .token(jwtToken)
                .build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void verifyAccount(String token) {
        // find the verification token in the database
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Verification token with token {" + token + "} was not found", NOT_FOUND, NOT_FOUND.value()));

        // set the user's active status to true and save the changes to the database
        User user = verificationToken.getUser();
        user.setActive(true);
        userRepository.save(user);

        log.info("✅✅✅ User is now activated. ✅✅✅");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate the user's credentials using the authentication manager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Get the user object from the authentication object and generate a JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found", NOT_FOUND, NOT_FOUND.value()));
        String jwtToken = jwtService.generateToken(user);

        // Revoke all the saved tokens for the user and save the generated token
        authenticationUtils.revokeAllUserTokens(user);
        authenticationUtils.buildAndSaveJwtToken(user, jwtToken);

        // Return the authentication response with the JWT token and user information
        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .roles(user.getRoles()
                        .stream().map(roles -> roles.getRole().name())
                        .collect(Collectors.toList()))
                .token(jwtToken)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getCurrentUser() {
        // Step 1: Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated())
            throw new AuthenticationFailedException("❌❌❌ User is not authenticated! ❌❌❌", UNAUTHORIZED, UNAUTHORIZED.value());

        // Step 2: Get the email of the authenticated user
        String email = authentication.getName();

        // Find the user object in the database using the email
        Optional<User> foundUser = userRepository.findByEmail(email);

        // Check if the user exists in the database
        return foundUser
                .orElseThrow(() ->
                        new ResourceNotFoundException("❌❌❌ User with email: '" + email + "' not found! ❌❌❌", NOT_FOUND, 404)
                ).getUserId();
    }

}
