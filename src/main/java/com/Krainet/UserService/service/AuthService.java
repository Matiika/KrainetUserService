package com.Krainet.UserService.service;


import com.Krainet.UserService.JWT.JwtRequest;
import com.Krainet.UserService.JWT.JwtResponse;
import com.Krainet.UserService.configuration.CustomUserDetailsService;
import com.Krainet.UserService.configuration.JwtUtil;
import com.Krainet.UserService.dto.UserCreateDTO;
import com.Krainet.UserService.dto.UserDTO;
import com.Krainet.UserService.mapper.UserMapStructMapper;
import com.Krainet.UserService.model.User;
import com.Krainet.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapStructMapper userMapStructMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public JwtResponse authenticate(JwtRequest jwtRequest) throws Exception {
        authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByUsername(jwtRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return new JwtResponse(token, userMapStructMapper.toDTO(user));
    }

    public UserDTO register(UserCreateDTO userCreateDTO) {
        // Проверяем, существует ли пользователь с таким username
        if (userRepository.findByUsername(userCreateDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        // Проверяем, существует ли пользователь с таким email
        if (userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = userMapStructMapper.fromCreateDTO(userCreateDTO);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setRole("USER"); // По умолчанию роль USER

        userRepository.save(user);
        return userMapStructMapper.toDTO(user);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
