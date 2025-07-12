package com.raven.api.meetings.controllers;

import com.raven.api.meetings.dto.CreateRoomRequest;
import com.raven.api.meetings.dto.RoomResponse;
import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/meetings/rooms")
@RequiredArgsConstructor
public class RoomsController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRooms() {
        List<Room> rooms = roomService.getRooms();
        return ResponseEntity.status(HttpStatus.OK).body(roomService.toResponse(rooms));
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody CreateRoomRequest request) {
        Room emptyRoom = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.toResponse(emptyRoom));
    }
}
