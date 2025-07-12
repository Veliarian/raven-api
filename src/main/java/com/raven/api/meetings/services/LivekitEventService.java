package com.raven.api.meetings.services;

import com.raven.api.meetings.enums.LivekitRoomEvent;
import jakarta.transaction.Transactional;
import livekit.LivekitWebhook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivekitEventService {

    private final RoomService roomService;

    @Transactional
    public void handleRoomEvent(LivekitWebhook.WebhookEvent webhookEvent) {
        LivekitRoomEvent event = LivekitRoomEvent.fromEvent(webhookEvent.getEvent());

        switch (event) {
            case ROOM_STARTED:
                System.out.println("Room started: " + webhookEvent.getRoom().getName());
                break;

            case ROOM_FINISHED:
                System.out.println("Room finished: " + webhookEvent.getRoom().getName());
                roomService.deleteRoomBySid(webhookEvent.getRoom().getSid());
                break;

            default:
                System.out.println("Unhandled event: " + event);
        }
    }
}
