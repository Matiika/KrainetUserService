package com.Krainet.UserService.contoller;



import com.Krainet.UserService.JWT.JwtRequest;
import com.Krainet.UserService.JWT.JwtResponse;
import com.Krainet.UserService.dto.UserCreateDTO;
import com.Krainet.UserService.dto.UserDTO;

import com.Krainet.UserService.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Получить токен доступа", description = "🔐 Логин и пароли пользователей\n\n" +
            "👤 Администратор:\n\n" +
            "Логин: admin1\n\n" +
            "Пароль: admin123\n\n" +
            "👤 Пользователь:\n\n" +
            "Логин: user2\n\n" +
            "Пароль: user123")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) throws Exception {
        JwtResponse jwtResponse = authService.authenticate(jwtRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO register(@RequestBody UserCreateDTO userCreateDTO) {
        return authService.register(userCreateDTO);
    }

}
