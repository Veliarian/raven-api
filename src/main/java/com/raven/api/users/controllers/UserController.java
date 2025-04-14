package com.raven.api.users.controllers;

import com.raven.api.users.dto.UpdateRoleRequest;
import com.raven.api.users.dto.UpdateUserRequest;
import com.raven.api.users.entity.User;
import com.raven.api.users.dto.UserResponse;
import com.raven.api.users.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(userService.toResponse(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toResponse(user));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(userService.toResponse(user));
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file, @PathVariable Long id) {
        String fileName = userService.updateProfilePicture(file, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(fileName);
    }

    @GetMapping("/avatar/{picture}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String picture) {
        Resource resource = userService.getProfilePicture(picture);
        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        User user = userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(userService.toResponse(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRole(@PathVariable Long id, @RequestBody UpdateRoleRequest request) {
        userService.updateRole(id, request.getRole());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
