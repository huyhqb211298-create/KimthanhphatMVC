package com.example.kimthanhphatmvc.config;

import com.example.kimthanhphatmvc.model.UserEntity;
import com.example.kimthanhphatmvc.model.enums.Role;
import com.example.kimthanhphatmvc.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DefaultAdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            // Nếu admin đã tồn tại thì bỏ qua
            if (userRepository.findByUsername("admin").isPresent()) {
                return;
            }

            // Tạo admin tối thượng
            UserEntity admin = UserEntity.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("kimthanhphatpccc"))  // mật khẩu của bạn
                    .fullName("Super Admin")
                    .email("admin@localhost")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();

            userRepository.save(admin);

            System.out.println("==== ADMIN ĐÃ ĐƯỢC TẠO ====");
        };
    }
}
