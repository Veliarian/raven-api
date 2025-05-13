package com.raven.api.meetings.services;

import com.raven.api.meetings.dto.CreateRoomRequest;
import com.raven.api.meetings.dto.RoomResponse;
import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.exceptions.RoomCreateException;
import com.raven.api.meetings.repositories.RoomRepository;
import io.livekit.server.RoomServiceClient;
import livekit.LivekitModels;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository repository;

    @Value("${livekit.api.host}")
    private String LIVEKIT_API_HOST = "http://localhost:7880";

    @Value("${livekit.api.key}")
    private String LIVEKIT_API_KEY = "devkey";

    @Value("${livekit.api.secret}")
    private String LIVEKIT_API_SECRET = "secret";

    private static final Integer LIVEKIT_ROOM_MAX_EMPTY_TIMEOUT = 600; // 10 minutes

    private final RoomServiceClient roomServiceClient = RoomServiceClient.createClient(
            LIVEKIT_API_HOST,
            LIVEKIT_API_KEY,
            LIVEKIT_API_SECRET
    );

    public RoomResponse toResponse(Room room){
        return new RoomResponse(room);
    }

    private Room save(Room room){
        return repository.save(room);
    }

    public Room createEmptyRoom(CreateRoomRequest request) {
        if(request.getName() == null || request.getName().isBlank() ){
            throw new RoomCreateException("Room name cannot be empty or blank");
        }

        try{
            Call<LivekitModels.Room> call = roomServiceClient.createRoom(
                    request.getName(),
                    LIVEKIT_ROOM_MAX_EMPTY_TIMEOUT
            );
            Response<LivekitModels.Room> response = call.execute();
            LivekitModels.Room roomResponse = response.body();

            Room room = new Room();
            room.setName(roomResponse.getName());

            return save(room);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
