package com.raven.api.users.mapper;

import com.raven.api.users.dto.UserResponse;
import com.raven.api.users.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user){
        if(user == null) return null;

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setEmailVerified(user.getEmailVerified());
        response.setRole(user.getRole());

        Optional.ofNullable(user.getUserProfile()).ifPresent(profile -> {
            response.setFirstName(profile.getFirstname());
            response.setLastName(profile.getLastname());
            response.setPatronymic(profile.getPatronymic());
            response.setBio(profile.getBio());
        });

        Optional.ofNullable(user.getProfilePicture()).ifPresent(profilePicture -> {
           response.setProfilePicture(profilePicture.getImageName());
        });

        return response;
    }

    public List<UserResponse> toResponse(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
