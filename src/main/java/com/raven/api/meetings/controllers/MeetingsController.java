package com.raven.api.meetings.controllers;

import com.raven.api.meetings.dto.*;
import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.services.LivekitEventService;
import com.raven.api.meetings.services.MeetingsAuthenticationService;
import com.raven.api.meetings.services.RoomService;
import io.livekit.server.AccessToken;
import io.livekit.server.WebhookReceiver;
import livekit.LivekitWebhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/meetings")
public class MeetingsController {

    private final RoomService roomService;
    private final MeetingsAuthenticationService authenticationService;
    private final LivekitEventService livekitEventService;

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponse>> getRooms(){
        List<Room> rooms = roomService.getRooms();
        return ResponseEntity.status(HttpStatus.OK).body(roomService.toResponse(rooms));
    }

    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody CreateRoomRequest request) {
        Room emptyRoom = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.toResponse(emptyRoom));
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody CreateTokenRequest request) {
        AccessToken token = authenticationService.createToken(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.toResponse(token));
    }

    @PostMapping(value = "/webhook", consumes = {"application/webhook+json", "application/json"})
    public ResponseEntity<String> receiveWebhook(@RequestHeader("Authorization") String authHeader, @RequestBody String body) {
        System.out.println("Webhook received: " + body);
        WebhookReceiver webhookReceiver = new WebhookReceiver("devkey", "secret");

        try {
            LivekitWebhook.WebhookEvent event = webhookReceiver.receive(body, authHeader);
            livekitEventService.handleRoomEvent(event);
        } catch (Exception e) {
            System.err.println("Error handling event: " + e.getMessage());
        }
        return ResponseEntity.ok("ok");
    }
}
