package com.raven.api.meetings.dto;

import livekit.LivekitModels.Room;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LivekitWebhookDto {

    private String event;

    private Room room;
}
