package com.raven.api.auth.filters;

import com.raven.api.auth.services.JwtService;
import com.raven.api.users.entity.User;
import com.raven.api.users.entity.UserProfile;
import com.raven.api.users.enums.Role;
import com.raven.api.users.repositories.UserRepository;
import com.raven.api.users.services.UserService;
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

    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        User user = userService.create(oAuth2User);
        String token = jwtService.generateToken(user);

        response.sendRedirect("http://localhost:5173/oauth2/callback?token=" + token);
    }
}

