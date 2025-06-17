package com.raven.api.meetings.enums;

public enum LivekitRoomEvent {
    ROOM_STARTED("room_started"),
    ROOM_FINISHED("room_finished");

    private final String event;

    LivekitRoomEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public static LivekitRoomEvent fromEvent(String event) {
        for (LivekitRoomEvent e : values()) {
            if (e.event.equals(event)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown LiveKit event: " + event);
    }
}
