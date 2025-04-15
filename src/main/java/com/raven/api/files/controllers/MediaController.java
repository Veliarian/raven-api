package com.raven.api.files.controllers;

import com.raven.api.files.dto.MediaFileResponse;
import com.raven.api.files.entity.MediaFile;
import com.raven.api.files.services.MediaFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaFilesService mediaFilesService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MediaFileResponse>> getAllMediaFiles(){
        List<MediaFile> mediaFiles = mediaFilesService.getAllMediaFiles();
        return ResponseEntity.status(HttpStatus.OK).body(mediaFilesService.toResponse(mediaFiles));
    }

    @PostMapping
    public ResponseEntity<MediaFileResponse> uploadFile(@RequestParam MultipartFile file) {
        MediaFile mediaFile = mediaFilesService.upload(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaFilesService.toResponse(mediaFile));
    }

    @GetMapping("/user")
    public ResponseEntity<List<MediaFileResponse>> getAllMediaFilesByUser() {
        List<MediaFile> mediaFiles = mediaFilesService.getAllMediaFilesByUser();
        return ResponseEntity.status(HttpStatus.OK).body(mediaFilesService.toResponse(mediaFiles));
    }
}
