package com.example.Inventory.system;

import com.example.Inventory.system.entity.Role;
import com.example.Inventory.system.entity.User;
import com.example.Inventory.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class InventorySystemApplication {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(InventorySystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            // Create admin if not exists
            if (userRepository.findByEmail("admin@sims.com").isEmpty()) {
                User admin = User.builder()
                        .email("admin@sims.com")
                        .password(passwordEncoder.encode("admin123"))
                        .fullName("Admin User")
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(admin);
                System.out.println("✅ Admin user created: admin@sims.com / admin123");
            }

            // (Optional) Create a manager too
            if (userRepository.findByEmail("manager@sims.com").isEmpty()) {
                User manager = User.builder()
                        .email("manager@sims.com")
                        .password(passwordEncoder.encode("manager123"))
                        .fullName("Manager User")
                        .role(Role.MANAGER)
                        .build();
                userRepository.save(manager);
                System.out.println("✅ Manager user created: manager@sims.com / manager123");
            }
        };
    }
}
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class InventorySystemApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(InventorySystemApplication.class, args);
//	}
//
//}
