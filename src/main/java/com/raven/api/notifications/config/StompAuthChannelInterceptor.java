package com.raven.api.notifications.config;

import com.raven.api.auth.services.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collections;

@Component
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    public StompAuthChannelInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = firstNativeHeader(accessor, "Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing or invalid Authorization header in STOMP CONNECT");
            }

            String token = authHeader.substring(7);

            // 🔹 перевірка токена
            Claims claims = jwtService.extractAllClaims(token);

            // userId з claims (бо ти додаєш id/email/role в токен)
            Object userIdClaim = claims.get("id");
            String username = claims.getSubject(); // subject = username

            if (username == null || userIdClaim == null) {
                throw new IllegalArgumentException("Invalid JWT: missing subject or id");
            }

            // Робимо Principal з userId (можеш також покласти username)
            Principal principal = new UsernamePasswordAuthenticationToken(
                    userIdClaim.toString(), // principalName = id
                    null,
                    Collections.emptyList()
            );

            accessor.setUser(principal);
        }

        return message;
    }

    private static String firstNativeHeader(StompHeaderAccessor accessor, String name) {
        var values = accessor.getNativeHeader(name);
        return (values == null || values.isEmpty()) ? null : values.get(0);
    }
}

