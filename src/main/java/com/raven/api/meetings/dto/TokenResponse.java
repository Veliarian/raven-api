package com.raven.api.meetings.dto;

import io.livekit.server.AccessToken;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String token;

    public TokenResponse(AccessToken token) {
        this.token = token.toJwt();
    }
}
