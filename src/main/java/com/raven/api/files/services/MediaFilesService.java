package com.raven.api.files.services;

import com.raven.api.files.dto.MediaFileResponse;
import com.raven.api.files.entity.MediaFile;
import com.raven.api.files.enums.MediaType;
import com.raven.api.files.repositories.MediaFileRepository;
import com.raven.api.files.utils.MimeTypeUtils;
import com.raven.api.users.entity.User;
import com.raven.api.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaFilesService {
    @Value("${files.upload-dir}")
    private String uploadDir;

    private final MediaFileRepository repository;
    private final UserService userService;

    public MediaFileResponse toResponse(MediaFile mediaFile) {
        return new MediaFileResponse(mediaFile);
    }

    public List<MediaFileResponse> toResponse(List<MediaFile> mediaFiles) {
        return mediaFiles.stream().map(this::toResponse).collect(Collectors.toList());
    }

    private MediaFile save(MediaFile mediaFile) {
        return repository.save(mediaFile);
    }

    public List<MediaFile> getAllMediaFiles() {
        return repository.findAll();
    }

    public List<MediaFile> getAllMediaFilesByUser() {
        User user = userService.getCurrentUser();
        return user.getMediaFiles();
    }

    public MediaFile upload(MultipartFile file) {
        try{
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String extension = MimeTypeUtils.getExtension(file.getContentType());
            MediaType mediaType = MimeTypeUtils.getMediaType(file.getContentType());
            String uniqueFileName = UUID.randomUUID() + (originalFileName != null ? "_" + originalFileName : extension);
            Path filePath = uploadPath.resolve(uniqueFileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            User user = userService.getCurrentUser();

            MediaFile mediaFile = new MediaFile();
            mediaFile.setOriginalName(originalFileName);
            mediaFile.setStoreName(uniqueFileName);
            mediaFile.setContentType(file.getContentType());
            mediaFile.setMediaType(mediaType);
            mediaFile.setSize(file.getSize());
            mediaFile.setUploadedBy(user);
            mediaFile.setUploadedAt(LocalDateTime.now());
            mediaFile.setPublic(false);

            return save(mediaFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
