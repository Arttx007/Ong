package com.poramordemuitos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.*;

@SuppressWarnings("ALL")
@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN").build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/beneficiarios", "/api/beneficiarios/**", "/uploads/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/beneficiarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/beneficiarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/beneficiarios/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .httpBasic();
        return http.build();
    }
}