package com.poramordemuitos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.HttpStatusEntryPoint; // NOVO IMPORT
import org.springframework.http.HttpStatus; // NOVO IMPORT
import java.util.Arrays;

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

    // --- CONFIGURAÇÃO DE CORS ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite a origem do seu projeto (http://localhost:8080)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*")); 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }

    /**
     * Configuração principal da cadeia de filtros de segurança.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. ATIVA O CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 2. DESABILITA CSRF
            .csrf(AbstractHttpConfigurer::disable) 
            
            // 3. CONFIGURAÇÃO DE EXCEÇÕES (Resolvendo o erro de token '<')
            // Se o usuário não estiver autenticado e tentar acessar uma API protegida,
            // retorna 401 (Unauthorized) em vez de HTML de redirecionamento.
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            
            .authorizeHttpRequests(authz -> authz
                
                // ROTAS PÚBLICAS
                .requestMatchers("/", 
                                 "/index.html",
                                 "/css/**", 
                                 "/js/**", 
                                 "/src/**",
                                 "/pages/**").permitAll()
                
                // ROTAS API PÚBLICAS
                .requestMatchers(HttpMethod.GET, "/api/beneficiarios", "/api/beneficiarios/**", "/uploads/**").permitAll()
                
                // ROTAS PROTEGIDAS (ADMIN)
                .requestMatchers(HttpMethod.POST, "/api/beneficiarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/beneficiarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/beneficiarios/**").hasRole("ADMIN")
                
                // QUALQUER OUTRA ROTA: Exige autenticação
                .anyRequest().authenticated()
            )
            
            // CONFIGURAÇÃO DE LOGIN
            .formLogin(login -> login
                .loginPage("/pages/login.html")
                // Trata a falha de credenciais inválidas com 401
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); 
                })
                .permitAll()
            );
                
        return http.build();
    }
}
