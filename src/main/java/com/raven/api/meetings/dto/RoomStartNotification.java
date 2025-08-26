package com.raven.api.meetings.dto;

import com.raven.api.meetings.entity.RoomNotification;
import com.raven.api.meetings.enums.RoomStatus;
import lombok.*;


public record RoomStartNotification(Long roomId, RoomStatus status, String code) {
    public static RoomStartNotification from(RoomNotification notification) {
        return new RoomStartNotification(notification.getRoomId(), notification.getRoomStatus(), notification.getCode());
    }
}
