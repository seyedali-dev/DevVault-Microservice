package com.dev.vault.authenticationservice.config.handler;

import com.dev.vault.authenticationservice.model.entity.jwt.JwtToken;
import com.dev.vault.authenticationservice.repository.jwt.JwtTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {

    @Value("${token.prefix}")
    private String TOKEN_PREFIX;

    private final JwtTokenRepository jwtTokenRepository;

    /**
     * Logs out the user by invalidating their JWT token and deleting all their saved tokens.
     * This method is called when the user sends a logout request to the server.
     *
     * @param request        the HTTP servlet request
     * @param response       the HTTP servlet response
     * @param authentication the authentication object for the user
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");

        // If the header is missing or does not start with the token prefix, log an error and return
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            log.error("❌❌❌ Invalid token or header = null ❌❌❌");
            return;
        }

        final String token = authHeader.substring(TOKEN_PREFIX.length()); // which is 7
        JwtToken jwtToken = jwtTokenRepository.findByToken(token).orElse(null);

        // If the token is found, get the user ID and delete all their saved tokens from the database
        if (jwtToken != null) {
            Long userId = jwtToken.getUser().getUserId();
            jwtTokenRepository.findAllByUser_UserId(userId)
                    .forEach(savedToken ->
                            jwtTokenRepository.deleteById(savedToken.getJwtTokenId())
                    );
            log.warn("User {} has logged out ✅. Proceeding the delete his/her tokens ...", jwtToken.getUser().getUsername());
        }
    }

}
