package com.poramordemuitos.config;

import com.poramordemuitos.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Bean para criptografia de senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configura o AuthenticationManager usando o CustomUserDetailsService
    @Bean
    public AuthenticationManager authManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new org.springframework.security.authentication.ProviderManager(provider);
    }

    // Configuração de segurança HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Desativa CSRF para testes
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas
                .requestMatchers(
                    "/", 
                    "/index.html", 
                    "/pages/login.html", 
                    "/css/**", 
                    "/js/**", 
                    "/img/**", 
                    "/uploads/**"
                ).permitAll()
                
                // Rotas administrativas
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Qualquer outra rota exige autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/pages/login.html")       // Página de login customizada
                .loginProcessingUrl("/login")         // URL que processa login
                .defaultSuccessUrl("/admin/dashboard.html", true) // Redirecionamento após login
                .failureUrl("/pages/login.html?error") // Redireciona em caso de erro
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/pages/login.html")
                .permitAll()
            );

        return http.build();
    }
}
