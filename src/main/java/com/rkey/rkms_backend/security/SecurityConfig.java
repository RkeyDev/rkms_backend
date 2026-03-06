package com.rkey.rkms_backend.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("CRITICAL: Custom Security Config Loaded Successfully!"); 
        
        http
            // Hook in our custom CORS configuration source defined below
            .cors(Customizer.withDefaults()) 
            
            // CSRF remains disabled for token-based/stateless REST APIs
            .csrf(AbstractHttpConfigurer::disable)
            
            // Endpoint authorization mapping
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll() 
                .anyRequest().authenticated()
            );
        
        return http.build();
    }

    /**
     * Global CORS configuration mapped directly into Spring Security.
     * Handles browser preflight (OPTIONS) requests safely.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // TODO: For production, externalize value to application.yml via @Value
        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173"));
        
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        
        // Required when the frontend uses withCredentials: true
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this CORS config to all API endpoints
        source.registerCorsConfiguration("/api/**", configuration); 
        
        return source;
    }
}