package com.dev.vault.authenticationservice.controller.auth;

import com.dev.vault.authenticationservice.model.request.AuthenticationRequest;
import com.dev.vault.authenticationservice.model.request.RegisterRequest;
import com.dev.vault.authenticationservice.service.auth.AuthenticationService;
import com.dev.vault.shared.lib.model.response.ApiResponse;
import com.dev.vault.shared.lib.model.response.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The AuthenticationController class is REST controller that handles the authentication and authorization of users.
 * It contains methods for registering a new user, verifying a user's account, and authenticating a user.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    /**
     * The register method registers a new user with the system.
     * It takes a RegisterRequest object as input and returns an AuthenticationResponse object.
     * The method is annotated with @PostMapping and @Valid to handle HTTP POST requests and validate the input.
     *
     * @param registerRequest The RegisterRequest object containing the user's information.
     * @return The AuthenticationResponse object containing the user's authentication token.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        return new ResponseEntity<>(authenticationService.registerUser(registerRequest), HttpStatus.CREATED);
    }


    /**
     * The verifyAccount method verifies a user's account using a token.
     * It takes a token as input and returns an ApiResponse object.
     * The method is annotated with @RequestMapping to handle HTTP GET and POST requests.
     *
     * @param token The token used to verify the user's account.
     * @return The ApiResponse object containing the result of the verification.
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "/accountVerification/{token}")
    public ResponseEntity<ApiResponse> verifyAccount(@PathVariable String token) {
        authenticationService.verifyAccount(token);
        return new ResponseEntity<>(new ApiResponse("account activated successfully", true), HttpStatus.OK);
    }


    /**
     * The authenticate method authenticates a user with the system.
     * It takes an AuthenticationRequest object as input and returns an AuthenticationResponse object.
     * The method is annotated with @PostMapping to handle HTTP POST requests.
     *
     * @param authenticationRequest The AuthenticationRequest object containing the user's credentials.
     * @return The AuthenticationResponse object containing the user's authentication token.
     */
    @PostMapping({"/login", "/authenticate"})
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

}
