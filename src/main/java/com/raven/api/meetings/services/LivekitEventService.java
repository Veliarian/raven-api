package com.raven.api.meetings.services;

import com.raven.api.meetings.dto.LivekitWebhookDto;
import com.raven.api.meetings.enums.LivekitRoomEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivekitEventService {

    private final RoomService roomService;

    public void handleRoomEvent(LivekitWebhookDto dto) {
        LivekitRoomEvent event = LivekitRoomEvent.fromEvent(dto.getEvent());

        switch (event) {
            case ROOM_STARTED:
                System.out.println("Room started: " + dto.getRoom().getName());
                break;

            case ROOM_FINISHED:
                System.out.println("Room finished: " + dto.getRoom().getName());
                roomService.deleteRoomBySid(dto.getRoom().getSid());
                break;

            default:
                System.out.println("Unhandled event: " + event);
        }
    }
}
