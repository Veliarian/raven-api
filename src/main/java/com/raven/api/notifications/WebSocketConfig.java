package com.raven.api.notifications;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // endpoint для підключення з клієнта (Vue/React/JS)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // дозволяємо будь-який origin
                .withSockJS(); // якщо треба підтримка старих браузерів
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // куди можна надсилати повідомлення з клієнта
        registry.setApplicationDestinationPrefixes("/app");
        // звідки клієнт отримає повідомлення
        registry.enableSimpleBroker("/topic");
    }
}
