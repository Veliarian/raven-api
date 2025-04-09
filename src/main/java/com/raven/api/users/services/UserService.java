package com.raven.api.users.services;

import com.raven.api.tools.exceptions.ForbiddenException;
import com.raven.api.users.dto.UpdateUserRequest;
import com.raven.api.users.dto.UserResponse;
import com.raven.api.users.enums.Role;
import com.raven.api.users.exceptions.UserAlreadyExistsException;
import com.raven.api.users.entity.User;
import com.raven.api.users.exceptions.UserNotFoundException;
import com.raven.api.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserResponse toResponse(User user) {
        return new UserResponse(user);
    }

    public List<UserResponse> toResponse(List<User> users) {
        return users.stream().map(this::toResponse).collect(Collectors.toList());
    }

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

        if(!currentUser.getId().equals(id) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
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

        if (request.getFirstName() != null && !request.getFirstName().equals(updatedUser.getUserProfile().getFirstName())) {
            updatedUser.getUserProfile().setFirstName(request.getFirstName());
            updated = true;
        }

        if (request.getLastName() != null && !request.getLastName().equals(updatedUser.getUserProfile().getLastName())) {
            updatedUser.getUserProfile().setLastName(request.getLastName());
            updated = true;
        }

        if (request.getPatronymic() != null && !request.getPatronymic().equals(updatedUser.getUserProfile().getPatronymic())) {
            updatedUser.getUserProfile().setPatronymic(request.getPatronymic());
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
        // Получение имени пользователя из контекста Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}
