package com.raven.api.meetings.controllers;

import com.raven.api.meetings.dto.CreateTokenRequest;
import com.raven.api.meetings.dto.TokenResponse;
import com.raven.api.meetings.services.LivekitEventService;
import com.raven.api.meetings.services.MeetingsAuthenticationService;
import io.livekit.server.AccessToken;
import io.livekit.server.WebhookReceiver;
import livekit.LivekitWebhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/meetings")
public class MeetingsController {

    @Value("${livekit.api.key}")
    private String LIVEKIT_API_KEY;

    @Value("${livekit.api.secret}")
    private String LIVEKIT_API_SECRET;

    private final MeetingsAuthenticationService authenticationService;
    private final LivekitEventService livekitEventService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody CreateTokenRequest request) {
        AccessToken token = authenticationService.createToken(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.toResponse(token));
    }

    @PostMapping(value = "/webhook", consumes = {"application/webhook+json", "application/json"})
    public ResponseEntity<String> receiveWebhook(@RequestHeader("Authorization") String authHeader, @RequestBody String body) {
        System.out.println("Webhook received: " + body);
        WebhookReceiver webhookReceiver = new WebhookReceiver(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);

        try {
            LivekitWebhook.WebhookEvent event = webhookReceiver.receive(body, authHeader);
            livekitEventService.handleRoomEvent(event);
        } catch (Exception e) {
            System.err.println("Error handling event: " + e.getMessage());
        }
        return ResponseEntity.ok("ok");
    }
}
