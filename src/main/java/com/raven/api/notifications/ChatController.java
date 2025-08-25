package com.raven.api.notifications;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    @MessageMapping("/hello")       // клієнт надсилає на /app/hello
    @SendTo("/topic/greetings")     // повідомлення підуть усім підписаним на /topic/greetings
    public String greeting(String message) {
        return "Привіт від сервера: " + message;
    }
}