package com.raven.api.auth.filters;

import com.raven.api.auth.services.JwtService;
import com.raven.api.users.entity.User;
import com.raven.api.users.enums.Role;
import com.raven.api.users.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String givenName = oAuth2User.getAttribute("given_name");
        String familyName = oAuth2User.getAttribute("family_name");
        Boolean emailVerified = oAuth2User.getAttribute("email_verified");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(email);
                    newUser.setEmailVerified(emailVerified);
                    newUser.setRole(Role.ROLE_USER);

                    newUser.getUserProfile().setFirstName(givenName);
                    newUser.getUserProfile().setLastName(familyName);
                    return userRepository.save(newUser);
                });

        String token = jwtService.generateToken(user);

        response.sendRedirect("http://localhost:5173/oauth2/callback?token=" + token);
    }
}

