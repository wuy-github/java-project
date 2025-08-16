package com.project.futabuslines.configurations;

import com.project.futabuslines.models.User;
import com.project.futabuslines.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    // User's Detail Object
    @Bean
    public UserDetailsService userDetailsService(){
        return identifier -> {
            Optional<User> optionalUser;
            if (identifier.contains("@")) {
                optionalUser = userRepository.findByEmail(identifier);
            } else {
                optionalUser = userRepository.findByPhoneNumber(identifier);
            }

            return optionalUser.orElseThrow(() ->
                    new UsernameNotFoundException("Không tìm thấy người dùng với thông tin: " + identifier));
        };
    }

    // Ma hoa mat khau
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    )throws Exception{
        return config.getAuthenticationManager();
    }
}
