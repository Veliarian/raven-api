package com.raven.api.users.services;

import com.raven.api.users.entity.ProfilePicture;
import com.raven.api.users.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;


@Service
public class ProfilePictureService {
    @Value("${files.avatar-dir}")
    private String avatarDir;

    public ProfilePicture savePictureFromGoogle(String pictureUrl) {
        try {
            Path uploadPath = Paths.get(avatarDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(pictureUrl)).build();
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() != 200) {
                throw new IOException("Failed to download image: " + response.statusCode());
            }

            String contentType = response.headers().firstValue("Content-Type").orElse("");
            String extension = getExtension(contentType);

            String fileName = UUID.randomUUID() + "_avatar" + extension;
            Path filePath = uploadPath.resolve(fileName);

            Files.write(filePath, response.body(), StandardOpenOption.CREATE);

            ProfilePicture profilePicture = new ProfilePicture();
            profilePicture.setImageName(fileName);

            return profilePicture;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ProfilePicture updatePicture(MultipartFile file, ProfilePicture profilePicture) {
        try{
            Path uploadPath = Paths.get(avatarDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String oldImage = profilePicture.getImageName();
            if(oldImage != null) {
                Path oldImagePath = uploadPath.resolve(oldImage);
                Files.deleteIfExists(oldImagePath);
            }

            String extension = getExtension(file.getContentType());
            String fileName = UUID.randomUUID() + "_avatar" + extension;
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            profilePicture.setImageName(fileName);

            return profilePicture;
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getExtension(String contentType) throws IOException {
        if(contentType == null) {
            throw new IllegalArgumentException("Content type cannot be null");
        }

        return switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new IOException("Unsupported image type: " + contentType);
        };
    }
}
