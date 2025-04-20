package com.raven.api.files.dto;

import com.raven.api.files.entity.MediaFile;
import com.raven.api.files.enums.MediaType;
import com.raven.api.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaFileResponse {

    private Long id;

    private String originalName;

    private String storeName;

    private String contentType;

    private Long size;

    private MediaType mediaType;

    private LocalDateTime uploadedAt;

    private Long uploadedBy;

    private boolean isPublic;

    public MediaFileResponse(MediaFile mediaFile) {
        this.id = mediaFile.getId();
        this.originalName = mediaFile.getOriginalName();
        this.storeName = mediaFile.getStoreName();
        this.contentType = mediaFile.getContentType();
        this.mediaType = mediaFile.getMediaType();
        this.size = mediaFile.getSize();
        this.uploadedAt = mediaFile.getUploadedAt();
        this.uploadedBy = mediaFile.getUploadedBy().getId();
        this.isPublic = mediaFile.isPublic();
    }
}
