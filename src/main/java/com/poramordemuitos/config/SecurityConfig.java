package com.poramordemuitos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 1. Password Encoder - Usando BCrypt para criptografia de senhas (Recomendado)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 2. UserDetailsService - Configuração temporária em memória (admin/123)
    @Bean
    public UserDetailsService inMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
        // Criando usuário de teste ADMIN com a senha codificada pelo BCrypt
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123"))
                .roles("ADMIN")
                .build();
                
        // Gerenciador de detalhes de usuário em memória
        return new InMemoryUserDetailsManager(admin);
    }
    
    // 3. Authentication Manager - Corrigido para evitar o erro de dependência.
    // Força o uso do nosso InMemoryUserDetailsService.
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService inMemoryUserDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(inMemoryUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        // Usa ProviderManager para garantir que apenas este provedor seja usado.
        return new ProviderManager(authProvider);
    }

    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Desativa CSRF
            .csrf(AbstractHttpConfigurer::disable) 
            
            // 2. Configura as regras de autorização
            .authorizeHttpRequests(authorize -> authorize 
                // Rotas ABERTAS (Públicas)
                .requestMatchers(
                    "/", 
                    "/index.html", 
                    "/pages/login.html", 
                    "/api/beneficiarios", 
                    "/api/beneficiarios/**", 
                    "/uploads/**",
                    "/css/**",
                    "/js/**",
                    "/img/**" 
                ).permitAll() 
                
                // Rotas PROTEGIDAS (Apenas para ADMIN)
                // Acesso ao dashboard no caminho simplificado (static/admin/dashboard.html)
                .requestMatchers("/admin/dashboard.html").hasRole("ADMIN") 
                .requestMatchers(HttpMethod.POST, "/api/beneficiarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/beneficiarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/beneficiarios/**").hasRole("ADMIN")
                
                // Qualquer outra URL precisa de autenticação
                .anyRequest().authenticated() 
            )
            
            // 3. ATIVA O FORMULÁRIO DE LOGIN CUSTOMIZADO
            .formLogin(form -> form
                .loginPage("/pages/login.html") 
                .loginProcessingUrl("/login") 
                // Redireciona para o novo caminho simplificado e força o redirecionamento.
                .defaultSuccessUrl("/admin/dashboard.html", true) 
                .failureUrl("/pages/login.html?error") 
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
