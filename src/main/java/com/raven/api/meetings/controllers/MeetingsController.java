package com.raven.api.meetings.controllers;

import com.raven.api.meetings.dto.CreateRoomRequest;
import com.raven.api.meetings.dto.CreateTokenRequest;
import com.raven.api.meetings.dto.RoomResponse;
import com.raven.api.meetings.dto.TokenResponse;
import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.services.MeetingsAuthenticationService;
import com.raven.api.meetings.services.RoomService;
import io.livekit.server.AccessToken;
import io.livekit.server.WebhookReceiver;
import livekit.LivekitWebhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/meetings")
public class MeetingsController {

    private final RoomService roomService;
    private final MeetingsAuthenticationService authenticationService;

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponse>> getRooms(){
        List<Room> rooms = roomService.getRooms();
        return ResponseEntity.status(HttpStatus.OK).body(roomService.toResponse(rooms));
    }

    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> createEmptyRoom(@RequestBody CreateRoomRequest request) {
        Room emptyRoom = roomService.createEmptyRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.toResponse(emptyRoom));
    }

    @PostMapping(value = "/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody CreateTokenRequest request) {
        System.out.println(request.getParticipantName());
        System.out.println(request.getRoomName());
        AccessToken token = authenticationService.createToken(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.toResponse(token));
    }

    @PostMapping(value = "/livekit/webhook", consumes = "application/webhook+json")
    public ResponseEntity<String> receiveWebhook(@RequestHeader("Authorization") String authHeader, @RequestBody String body) {
        WebhookReceiver webhookReceiver = new WebhookReceiver("devkey", "secret");
        try {
            LivekitWebhook.WebhookEvent event = webhookReceiver.receive(body, authHeader);
            System.out.println("LiveKit Webhook: " + event.toString());
        } catch (Exception e) {
            System.err.println("Error validating webhook event: " + e.getMessage());
        }
        return ResponseEntity.ok("ok");
    }
}
