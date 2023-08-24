package com.dev.vault.authenticationservice.util;

import com.dev.vault.authenticationservice.model.entity.User;
import com.dev.vault.authenticationservice.model.entity.VerificationToken;
import com.dev.vault.authenticationservice.model.entity.jwt.JwtToken;
import com.dev.vault.authenticationservice.model.entity.jwt.TokenType;
import com.dev.vault.authenticationservice.repository.VerificationTokenRepository;
import com.dev.vault.authenticationservice.repository.jwt.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A utility class that provides helper methods for authentication and registration of user.
 * This class contains method for revoking the JWT token, build and saving JWT token, generating verification token.
 */
@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class AuthenticationUtils {

    private final VerificationTokenRepository verificationTokenRepository;
    private final JwtTokenRepository jwtTokenRepository;

    /**
     * Builds and saves a JWT token for the specified user.
     *
     * @param user  the {@link User User} object for which to generate the JWT token
     * @param token the JWT token to save
     */
    public void buildAndSaveJwtToken(User user, String token) {
        // Build a new JwtToken object with the specified user and token, and save it to the database
        JwtToken jwtToken = JwtToken.builder()
                .expired(false)
                .revoked(false)
                .type(TokenType.BEARER)
                .user(user)
                .token(token)
                .build();
        jwtTokenRepository.save(jwtToken);
    }


    /**
     * Revokes all valid tokens for the specified user by setting their 'expired' and 'revoked' flags to true.
     *
     * @param user the {@link User User} object for which to revoke all tokens
     */
    public void revokeAllUserTokens(User user) {
        // Find all valid tokens for the specified user
        List<JwtToken> validUserTokens = jwtTokenRepository.findAllByUser_UserIdAndExpiredIsFalseAndRevokedIsFalse(user.getUserId());

        // If no valid tokens were found, return without modifying the database
        if (validUserTokens.isEmpty())
            return;

        // Iterate through all valid tokens and set their 'expired' and 'revoked' flags to true
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtTokenRepository.saveAll(validUserTokens);
    }


    /**
     * Generates a verification token for the user that has attempted to sign-up.
     *
     * @param user the user to generate the verification token for
     * @return the verification token as a string
     */
    public String generateVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken(user);
        verificationTokenRepository.save(verificationToken);
        return verificationToken.getToken();
    }

}
