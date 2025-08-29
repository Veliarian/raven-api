package com.raven.api.meetings.dto;

import com.raven.api.meetings.enums.RoomStatus;

public record RoomStartPayload (
        Long roomId,
        RoomStatus status
) {}
