package com.raven.api.meetings.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MeetingNotificationCode {
    ROOM_ACTIVATED("meeting.notification.room.activated"),
    ROOM_CREATED("meeting.notification.room.created");

    private final String code;
}
