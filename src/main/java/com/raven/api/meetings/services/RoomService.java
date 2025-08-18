package com.raven.api.meetings.services;

import com.raven.api.meetings.dto.CreateRoomRequest;
import com.raven.api.meetings.dto.RoomResponse;
import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.enums.RoomStatus;
import com.raven.api.meetings.exceptions.RoomCreateException;
import com.raven.api.meetings.repositories.RoomRepository;
import com.raven.api.notifications.entity.Notification;
import com.raven.api.notifications.enums.NotificationType;
import com.raven.api.notifications.services.NotificationService;
import com.raven.api.users.entity.User;
import com.raven.api.users.services.UserService;
import io.livekit.server.RoomServiceClient;
import jakarta.annotation.PostConstruct;
import livekit.LivekitModels;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository repository;
    private final RoomRepository roomRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private RoomServiceClient roomServiceClient;

    @Value("${livekit.api.host}")
    private String LIVEKIT_API_HOST;

    @Value("${livekit.api.key}")
    private String LIVEKIT_API_KEY;

    @Value("${livekit.api.secret}")
    private String LIVEKIT_API_SECRET;

    private static final Integer LIVEKIT_ROOM_MAX_EMPTY_TIMEOUT = 600; // 10 minutes

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
        User user = userService.getCurrentUser();
        return roomRepository.findAllByParticipants_Id(user.getId());
    }

    public Room createRoom(CreateRoomRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RoomCreateException("Room name cannot be empty");
        }

        Room room = new Room();
        room.setName(request.getName());

        if (request.getStartTime() == null) {
            LivekitModels.Room roomResponse = callToCreateRoom(request.getName());

            room.setSid(roomResponse.getSid());
            room.setStatus(RoomStatus.ACTIVE);
        } else {
            room.setStatus(RoomStatus.SCHEDULED);
            room.setStartTime(request.getStartTime());

        }

        room.addParticipant(userService.getCurrentUser());
        if(request.getParticipantIds() != null && !request.getParticipantIds().isEmpty()){
            request.getParticipantIds().forEach(participantId -> {
               User user = userService.getById(participantId);
               room.addParticipant(user);
            });
        }

        return save(room);
    }

    public void deleteRoomBySid(String sid) {
        roomRepository.deleteRoomBySid(sid);
    }

    @Scheduled(fixedRate = 30000)
    public void activateScheduledRoom() {
        List<Room> scheduledRooms = roomRepository.findAllByStatusAndStartTimeBefore(RoomStatus.SCHEDULED, LocalDateTime.now());

        for (Room scheduledRoom : scheduledRooms) {
            LivekitModels.Room roomResponse = callToCreateRoom(scheduledRoom.getName());

            scheduledRoom.setStatus(RoomStatus.ACTIVE);
            scheduledRoom.setSid(roomResponse.getSid());
            scheduledRoom.setStartTime(null);
            save(scheduledRoom);

            Notification notification = new Notification();
            notification.setTitle("Room started");
            notification.setMessage("Room " + scheduledRoom.getName() + " has been started");
            notification.setType(NotificationType.LESSON);
            notificationService.sendToUser(1L, notification);
        }
    }

    private LivekitModels.Room callToCreateRoom(String name) {
        roomServiceClient = RoomServiceClient.createClient(
                LIVEKIT_API_HOST,
                LIVEKIT_API_KEY,
                LIVEKIT_API_SECRET
        );

        try{
            Call<LivekitModels.Room> call = roomServiceClient.createRoom(name, LIVEKIT_ROOM_MAX_EMPTY_TIMEOUT);
            Response<LivekitModels.Room> response = call.execute();

            if (!response.isSuccessful()) {
                throw new RoomCreateException("Error creating room: " + response.message());
            }

            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
