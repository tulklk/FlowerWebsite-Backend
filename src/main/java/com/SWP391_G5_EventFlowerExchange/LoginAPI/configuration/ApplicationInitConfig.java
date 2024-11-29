package com.SWP391_G5_EventFlowerExchange.LoginAPI.configuration;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.User;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.enums.Role;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(IUserRepository IUserRepository) {
        return args -> {
          if(IUserRepository.findByEmail("admin@gmail.com").isEmpty()) {
              var roles= new HashSet<String>();
              roles.add(Role.ADMIN.name());

              User user= User.builder()
                      .username("admin")
                      .email("admin@gmail.com")
                      .roles(roles)
                      .password(passwordEncoder.encode("12345"))
                      .availableStatus("available")
                      .emailVerified(true)
                      .build();

              IUserRepository.save(user);

              log.warn("admin account has been created: admin@gmail.com, 12345");
          }
        };
    }
}
