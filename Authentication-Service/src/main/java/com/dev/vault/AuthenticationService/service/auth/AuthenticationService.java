package com.dev.vault.AuthenticationService.service.auth;

import com.dev.vault.shared.lib.exceptions.AuthenticationFailedException;
import com.dev.vault.shared.lib.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.shared.lib.exceptions.ResourceNotFoundException;
import com.dev.vault.AuthenticationService.model.request.AuthenticationRequest;
import com.dev.vault.AuthenticationService.model.request.RegisterRequest;
import com.dev.vault.shared.lib.model.response.AuthenticationResponse;

/**
 * Service class for authenticating and registering user.
 */
public interface AuthenticationService {

    /**
     * Registers a new user, assigns the <code>TEAM_MEMBER</code> role, saves the user to the database,
     * generates a verification token, sends an activation email, generates a JWT token,
     * saves the JWT token, and returns an {@link AuthenticationResponse AuthenticationResponse} object with the JWT token and user information.
     *
     * @param registerRequest the {@link RegisterRequest RegisterRequest} object containing the user's registration information
     * @return an {@link AuthenticationResponse AuthenticationResponse} object containing the JWT token and user information
     * @throws ResourceAlreadyExistsException if the user already exists in the database
     * @throws ResourceNotFoundException      if the role with {@code TEAM_MEMBER} role is not found
     */
    AuthenticationResponse registerUser(RegisterRequest registerRequest)
            throws ResourceAlreadyExistsException, ResourceNotFoundException;


    /**
     * Verifies the user's account and activates it.
     *
     * @param token the verification token
     */
    void verifyAccount(String token);


    /**
     * Authenticates the user's credentials and generates a JWT token.
     * Revokes all existing tokens for the user and saves the new token.
     *
     * @param authenticationRequest the authentication request containing the user's email and password
     * @return an AuthenticationResponse object containing the JWT token and user information
     * @throws ResourceNotFoundException if the user is not found with the given email
     */
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest)
            throws ResourceNotFoundException;


    /**
     * Retrieves the currently logged-in user.
     *
     * @return the logged-in user's ID
     * @throws AuthenticationFailedException if authentication was not successful i.e. email or password is wrong
     */
    Long getCurrentUser()
            throws AuthenticationFailedException;

}
