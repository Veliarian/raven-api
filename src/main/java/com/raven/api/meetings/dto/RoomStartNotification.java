package com.raven.api.meetings.dto;

import com.raven.api.meetings.entity.MeetingNotification;
import com.raven.api.meetings.enums.RoomStatus;

import java.util.Map;


public record RoomStartNotification(Long roomId, RoomStatus status, String code, Map<String, Object> params) {
    public static RoomStartNotification from(MeetingNotification notification) {
        return new RoomStartNotification(
                notification.getRoomId(),
                notification.getRoomStatus(),
                notification.getCode(),
                notification.getParams());
    }
}
