package com.ludicamente.Ludicamente.config;
import org.springframework.http.HttpMethod;
import com.ludicamente.Ludicamente.auth.userdetails.CompositeUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier; // No usado, se puede eliminar si no se usa en otra parte
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService; // No usado directamente, se puede eliminar si no se usa en otra parte
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@CrossOrigin
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CompositeUserDetailsService compositeUserDetailsService;
    private final JwtService jwtService;

    public SecurityConfig(CompositeUserDetailsService compositeUserDetailsService, JwtService jwtService) {
        this.compositeUserDetailsService = compositeUserDetailsService;
        this.jwtService = jwtService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(compositeUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Con la configuración actual de DaoAuthenticationProvider, puedes simplificarlo
        // a un ProviderManager que solo usa tu authenticationProvider.
        // O si Spring Boot 3.x te da un AuthenticationManager directamente, puedes usarlo.
        // Por ahora, este ProviderManager es correcto si authenticationProvider() es el único.
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, compositeUserDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HttpFirewall allowSpecialCharactersHttpFirewall) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso sin autenticación a la URL de subida de archivos
                        .requestMatchers(HttpMethod.GET, "/api/servicios/categoria/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias").permitAll()
                        .requestMatchers("/api/files/upload").permitAll() // <--- ¡AÑADIDO AQUÍ!
                        .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/error", "/favicon.ico", "/resources/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Asegúrate de que esta URL coincida con la URL de tu frontend de React
        // Si tu frontend está en http://localhost:5173, está bien.
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // Permite todos los headers, incluyendo Content-Type para form-data
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuración CORS a todas las rutas (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}