package com.Krainet.UserService.contoller;

import com.Krainet.UserService.dto.UserCreateDTO;
import com.Krainet.UserService.dto.UserDTO;
import com.Krainet.UserService.dto.UserUpdateDTO;
import com.Krainet.UserService.exeption.EmailAlreadyExistsException;
import com.Krainet.UserService.exeption.InvalidRoleException;
import com.Krainet.UserService.exeption.ResourceNotFoundException;
import com.Krainet.UserService.exeption.UserAlreadyExistsException;
import com.Krainet.UserService.mapper.UserMapStructMapper;
import com.Krainet.UserService.model.User;
import com.Krainet.UserService.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "User API",
        description = "🔐 Логин и пароли пользователей\n\n" +
                "👤 Администратор:\n\n" +
                "Логин: admin1\n\n" +
                "Пароль: admin123\n\n" +
                "👤 Пользователь:\n\n" +
                "Логин: user2\n\n" +
                "Пароль: user123"
)
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapStructMapper userMapStructMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Создание пользователя", security = @SecurityRequirement(name = ""))
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@RequestBody UserCreateDTO userCreateDTO) {
        // Проверяем, существует ли пользователь с таким username
        if (userRepository.findByUsername(userCreateDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с именем '" + userCreateDTO.getUsername() + "' уже существует");
        }

        // Проверяем, существует ли пользователь с таким email
        if (userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Пользователь с email '" + userCreateDTO.getEmail() + "' уже существует");
        }

        User user = userMapStructMapper.fromCreateDTO(userCreateDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return userMapStructMapper.toDTO(user);
    }

    @Operation(summary = "Получить всех пользователей", description = "Требуется авторизация: администратор")
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> index() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapStructMapper::toDTO)
                .toList();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("@userSecurityService.canAccessUser(authentication, #id)")
    @ResponseStatus(HttpStatus.OK)
    @Parameters({
            @Parameter(name = "id", description = "ID пользователя", example = "2")
    })
    public UserDTO show(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));
        return userMapStructMapper.toDTO(user);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("@userSecurityService.canAccessUser(authentication, #id)")
    @Parameters({
            @Parameter(name = "id", description = "ID пользователя", example = "2")
    })
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(
            @PathVariable Long id,
            @RequestBody UserUpdateDTO userUpdateDTO,
            Authentication authentication) {

        if (userUpdateDTO.getRole() != null &&
                !userUpdateDTO.getRole().equals("USER") &&
                !userUpdateDTO.getRole().equals("ADMIN")) {
            throw new InvalidRoleException("Роль должна быть USER или ADMIN");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));

        // Проверяем уникальность username при обновлении
        if (userUpdateDTO.getUsername() != null &&
                !userUpdateDTO.getUsername().equals(user.getUsername()) &&
                userRepository.findByUsername(userUpdateDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с именем '" + userUpdateDTO.getUsername() + "' уже существует");
        }

        // Проверяем уникальность email при обновлении
        if (userUpdateDTO.getEmail() != null &&
                !userUpdateDTO.getEmail().equals(user.getEmail()) &&
                userRepository.findByEmail(userUpdateDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Пользователь с email '" + userUpdateDTO.getEmail() + "' уже существует");
        }

        // Обычные пользователи не могут изменить свою роль
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            userUpdateDTO.setRole(null); // Игнорируем поле роли для не-админов
        }

        userMapStructMapper.updateEntityFromDTO(userUpdateDTO, user);

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        userRepository.save(user);
        return userMapStructMapper.toDTO(user);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("@userSecurityService.canAccessUser(authentication, #id)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}