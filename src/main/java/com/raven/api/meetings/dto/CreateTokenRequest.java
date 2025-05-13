package com.raven.api.meetings.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTokenRequest {
    private String roomName;
    private String participantName;
}
