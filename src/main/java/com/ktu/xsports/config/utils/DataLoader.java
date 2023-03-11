package com.ktu.xsports.config.utils;

import com.ktu.xsports.api.domain.User;
import com.ktu.xsports.api.util.Role;
import com.ktu.xsports.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements ApplicationRunner {

    private final static String ADMIN_EMAIL = "admin@email.com";
    private final static String ADMIN = "admin";
    private final static String ADMIN_BASE_PASSWORD = "ThePassword";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<User> admin = userRepository.findByEmail(ADMIN_EMAIL);
        if(admin.isEmpty()) {
            userRepository.save(User.builder()
                    .nickname(ADMIN)
                    .name(ADMIN)
                    .surname(ADMIN)
                    .email(ADMIN_EMAIL)
                    .password(passwordEncoder.encode(ADMIN_BASE_PASSWORD))
                    .photoUrl("none")
                    .role(Role.ADMIN)
                    .isBlocked(false)
                .build()
            );
        }
    }
}
