package com.raven.api.files.mapper;

import com.raven.api.files.dto.MediaFileResponse;
import com.raven.api.files.entity.MediaFile;
import com.raven.api.users.dto.UserResponse;
import com.raven.api.users.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MediaFileMapper {

    public MediaFileResponse toResponse(MediaFile mediaFile) {
        if(mediaFile == null) return null;

        MediaFileResponse response = new MediaFileResponse();

        response.setId(mediaFile.getId());
        response.setOriginalName(mediaFile.getOriginalName());
        response.setStoreName(mediaFile.getStoreName());
        response.setContentType(mediaFile.getContentType());
        response.setMediaType(mediaFile.getMediaType());
        response.setSize(mediaFile.getSize());
        response.setUploadedAt(mediaFile.getUploadedAt());
        response.setUploadedBy(mediaFile.getUploadedBy().getId());
        response.setPublic(mediaFile.isPublic());
        response.setInTrash(mediaFile.isInTrash());

        return response;
    }

    public List<MediaFileResponse> toResponse(List<MediaFile> mediaFiles) {
        return mediaFiles.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
