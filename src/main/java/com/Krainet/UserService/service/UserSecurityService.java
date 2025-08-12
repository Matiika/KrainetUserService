package com.Krainet.UserService.service;

import com.Krainet.UserService.model.User;
import com.Krainet.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService {

    @Autowired
    private UserRepository userRepository;

    public boolean canAccessUser(Authentication authentication, Long userId) {
        // Проверяем, аутентифицирован ли пользователь
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Если пользователь админ - разрешаем доступ ко всем
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return true;
        }

        // Для обычных пользователей проверяем, что это их собственный аккаунт
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return false;
        }

        return user.getId().equals(userId);
    }

}