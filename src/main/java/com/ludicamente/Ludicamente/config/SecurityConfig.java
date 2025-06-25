// ludicamente-backend/src/main/java/com/ludicamente/Ludicamente/config/SecurityConfig.java
package com.ludicamente.Ludicamente.config;
import org.springframework.http.HttpMethod;
import com.ludicamente.Ludicamente.auth.userdetails.CompositeUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier; // No usado, se puede eliminar si no se usa en otra parte

// Importaciones necesarias
import com.ludicamente.Ludicamente.auth.userdetails.CompositeUserDetailsService; // <-- ¡Asegúrate de que esta ruta de paquete sea EXACTA!
import com.ludicamente.Ludicamente.config.JwtService; // <-- Asegúrate de que esta ruta de paquete sea EXACTA para tu JwtService
import com.ludicamente.Ludicamente.config.JwtAuthenticationFilter; // <-- Asegúrate de que esta ruta de paquete sea EXACTA para tu JwtAuthenticationFilter

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// La anotación CrossOrigin en este archivo es redundante si tienes CorsConfigurationSource
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CompositeUserDetailsService compositeUserDetailsService;
    private final JwtService jwtService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor con inyección de dependencias
    public SecurityConfig(CompositeUserDetailsService compositeUserDetailsService, JwtService jwtService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.compositeUserDetailsService = compositeUserDetailsService;
        this.jwtService = jwtService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // ... el resto de tu clase SecurityConfig permanece igual ...

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // Aquí es donde el error ocurría si CompositeUserDetailsService no era un UserDetailsService
        provider.setUserDetailsService(compositeUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso sin autenticación a la URL de subida de archivos
                        .requestMatchers(HttpMethod.GET, "/api/servicios/categoria/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias").permitAll()
                        .requestMatchers("/api/files/upload").permitAll() // <--- ¡AÑADIDO AQUÍ!
                        .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/error", "/favicon.ico", "/resources/**").permitAll()
                        .requestMatchers(
                                "/api/chatbot/**",
                                "/api/files/upload",
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/error",
                                "/favicon.ico",
                                "/resources/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // O "http://localhost:3000"
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}