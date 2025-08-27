package com.raven.api.configuration;

import com.raven.api.auth.filters.AuthChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthChannelInterceptor authChannelInterceptor;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authChannelInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // куди можна надсилати повідомлення з клієнта
        registry.setApplicationDestinationPrefixes("/app");

        // звідки клієнт отримає повідомлення
        // Публічні топіки для групових повідомлень
        registry.enableSimpleBroker(
                "/topic",   // групові повідомлення
                "/board",   // інтерактивна дошка
                "/system"  // системні нотифікації
        );
        registry.setUserDestinationPrefix("/user");  // наприклад, /user/queue/notifications
    }
}
