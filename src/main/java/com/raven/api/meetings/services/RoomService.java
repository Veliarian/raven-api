package com.raven.api.meetings.services;

import com.raven.api.meetings.dto.CreateRoomRequest;
import com.raven.api.meetings.dto.RoomResponse;
import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.enums.RoomStatus;
import com.raven.api.meetings.exceptions.RoomCreateException;
import com.raven.api.meetings.repositories.RoomRepository;
import com.raven.api.users.services.UserService;
import io.livekit.server.RoomServiceClient;
import livekit.LivekitModels;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository repository;
    private final RoomRepository roomRepository;
    private final UserService userService;

    @Value("${livekit.api.host}")
    private String LIVEKIT_API_HOST = "http://localhost:7880";

    @Value("${livekit.api.key}")
    private String LIVEKIT_API_KEY = "devkey";

    @Value("${livekit.api.secret}")
    private String LIVEKIT_API_SECRET = "secret";

    private static final Integer LIVEKIT_ROOM_MAX_EMPTY_TIMEOUT = 120; // 2 minutes

    private final RoomServiceClient roomServiceClient = RoomServiceClient.createClient(
            LIVEKIT_API_HOST,
            LIVEKIT_API_KEY,
            LIVEKIT_API_SECRET
    );

    public RoomResponse toResponse(Room room) {
        return new RoomResponse(room);
    }

    public List<RoomResponse> toResponse(List<Room> rooms) {
        return rooms.stream().map(this::toResponse).collect(java.util.stream.Collectors.toList());
    }

    private Room save(Room room) {
        return repository.save(room);
    }

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public Room createRoom(CreateRoomRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RoomCreateException("Room name cannot be empty");
        }

        try {
            Call<LivekitModels.Room> call = roomServiceClient.createRoom(
                    request.getName(),
                    LIVEKIT_ROOM_MAX_EMPTY_TIMEOUT
            );
            Response<LivekitModels.Room> response = call.execute();

            if (!response.isSuccessful()) {
                throw new RoomCreateException("Error creating room: " + response.message());
            }

            LivekitModels.Room roomResponse = response.body();

            Room room = new Room();
            room.setSid(roomResponse.getSid());
            room.setName(roomResponse.getName());
            room.setStatus(RoomStatus.ACTIVE);
            room.addParticipant(userService.getCurrentUser());

            return save(room);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteRoomBySid(String sid) {
        roomRepository.deleteRoomBySid(sid);
    }
}
