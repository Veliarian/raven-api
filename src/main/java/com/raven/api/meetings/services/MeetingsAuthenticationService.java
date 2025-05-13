package com.raven.api.meetings.services;

import com.raven.api.meetings.dto.CreateTokenRequest;
import com.raven.api.meetings.dto.TokenResponse;
import com.raven.api.meetings.exceptions.MeetingTokenCreateException;
import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import io.livekit.server.VideoGrant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MeetingsAuthenticationService {

    private String LIVEKIT_API_KEY = "devkey";

    private String LIVEKIT_API_SECRET = "secret";

    public TokenResponse toResponse(AccessToken token) {
        return new TokenResponse(token);
    }

    public AccessToken createToken(CreateTokenRequest request) {
        if (request.getRoomName() == null || request.getParticipantName() == null) {
            throw new MeetingTokenCreateException("RoomName and ParticipantName are required");
        }

        AccessToken token = new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
        token.setName(request.getParticipantName());
        token.setIdentity(request.getParticipantName());
        token.addGrants(new RoomJoin(true), new RoomName(request.getRoomName()));

        return token;
    }
}
