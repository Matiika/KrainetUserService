package com.Krainet.UserService.configuration;

import com.Krainet.UserService.model.User;
import com.Krainet.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, есть ли уже пользователи в базе
        if (userRepository.count() == 0) {
            loadInitialData();
        }
    }

    private void loadInitialData() {
        System.out.println("Инициализация базы данных тестовыми пользователями...");

        // Создаем администратора
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@krainet.com");
        admin.setFirstName("Admin");
        admin.setLastName("Root");
        admin.setRole("ADMIN");
        userRepository.save(admin);

        // Создаем тестовых пользователей
        createUser("user1", "user123", "user1@krainet.com", "Ivan", "Petrov");
        createUser("user2", "user123", "user2@krainet.com", "Olga", "Sidorova");
        createUser("user3", "user123", "user3@krainet.com", "Dmitry", "Ivanov");

        System.out.println("Инициализация завершена. Создано пользователей: " + userRepository.count());

        // Выводим информацию для тестирования
        System.out.println("\n=== Данные для тестирования ===");
        System.out.println("ADMIN: username=admin, password=admin123");
        System.out.println("USER1: username=user1, password=user123");
        System.out.println("USER2: username=user2, password=user123");
        System.out.println("USER3: username=user3, password=user123");
    }

    private void createUser(String username, String password, String email, String firstName, String lastName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole("USER");
        userRepository.save(user);
    }

}
