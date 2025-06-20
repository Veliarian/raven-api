package com.raven.api.users.services;

import com.raven.api.tools.exceptions.FileNotFoundException;
import com.raven.api.tools.exceptions.ForbiddenException;
import com.raven.api.users.dto.UpdateUserRequest;
import com.raven.api.users.dto.UserResponse;
import com.raven.api.users.entity.ProfilePicture;
import com.raven.api.users.entity.UserProfile;
import com.raven.api.users.enums.Role;
import com.raven.api.users.exceptions.ProfilePictureNotFoundException;
import com.raven.api.users.exceptions.UserAlreadyExistsException;
import com.raven.api.users.entity.User;
import com.raven.api.users.exceptions.UserNotFoundException;
import com.raven.api.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Value("${files.avatar-dir}")
    private String avatarDir;

    private final UserRepository repository;
    private final ProfilePictureService profilePictureService;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("User with username " + user.getUsername() + " already exists");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        return save(user);
    }

    public User create(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String givenName = oAuth2User.getAttribute("given_name");
        String familyName = oAuth2User.getAttribute("family_name");
        Boolean emailVerified = oAuth2User.getAttribute("email_verified");

        String pictureUrl = oAuth2User.getAttribute("picture");

        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setEmailVerified(emailVerified);
            newUser.setRole(Role.ROLE_USER);

            UserProfile profile = new UserProfile();
            profile.setFirstname(givenName);
            profile.setLastname(familyName);

            newUser.setUserProfile(profile);

            if (pictureUrl != null && !pictureUrl.isEmpty()) {
                newUser.setProfilePicture(profilePictureService.savePictureFromGoogle(pictureUrl));
            }

            return save(newUser);
        });
    }

    public User getById(Long id) {
        User currentUser = getCurrentUser();

        if (!currentUser.getId().equals(id) &&
                !currentUser.getRole().equals(Role.ROLE_ADMIN) &&
                !currentUser.getRole().equals(Role.ROLE_TEACHER)) {
            throw new ForbiddenException("You are not allowed to access this resource");
        }

        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User updateUser(Long id, UpdateUserRequest request) {
        User currentUser = getCurrentUser();

        if (!currentUser.getId().equals(id) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new ForbiddenException("You are not allowed to access this resource");
        }

        User updatedUser = getById(id);
        boolean updated = false;

        if (request.getUsername() != null && !request.getUsername().equals(updatedUser.getUsername())) {
            updatedUser.setUsername(request.getUsername());
            updated = true;
        }

        if (request.getPassword() != null && !request.getPassword().equals(updatedUser.getPassword())) {
            updatedUser.setPassword(request.getPassword());
            updated = true;
        }

        if (request.getEmail() != null && !request.getEmail().equals(updatedUser.getEmail())) {
            updatedUser.setEmail(request.getEmail());
            updated = true;
        }

        if (request.getFirstname() != null && !request.getFirstname().equals(updatedUser.getUserProfile().getFirstname())) {
            updatedUser.getUserProfile().setFirstname(request.getFirstname());
            updated = true;
        }

        if (request.getLastname() != null && !request.getLastname().equals(updatedUser.getUserProfile().getLastname())) {
            updatedUser.getUserProfile().setLastname(request.getLastname());
            updated = true;
        }

        if (request.getPatronymic() != null && !request.getPatronymic().equals(updatedUser.getUserProfile().getPatronymic())) {
            updatedUser.getUserProfile().setPatronymic(request.getPatronymic());
            updated = true;
        }

        if (!request.getBio().equals(updatedUser.getUserProfile().getBio())) {
            updatedUser.getUserProfile().setBio(request.getBio());
            updated = true;
        }

        return updated ? repository.save(updatedUser) : updatedUser;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public User updateRole(Long id, Role role) {
        User user = getById(id);
        user.setRole(role);
        return save(user);
    }

    public String updateProfilePicture(MultipartFile file, Long userId) {
        User user = getById(userId);

        ProfilePicture current = user.getProfilePicture();

        ProfilePicture updated = profilePictureService.updatePicture(file,
                current != null ? current : new ProfilePicture());

        user.setProfilePicture(updated);
        save(user);

        return updated.getImageName();
    }

    public Resource getProfilePicture(String picture) {
        try {
            Path filePath = Paths.get(avatarDir).resolve(picture);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Could not read file: " + picture);
            }
        } catch (IOException e) {
            throw new FileNotFoundException("Could not read file: " + picture);
        }
    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
