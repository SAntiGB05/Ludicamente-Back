package com.ludicamente.Ludicamente.config;

// Importaciones necesarias
import com.ludicamente.Ludicamente.auth.userdetails.CompositeUserDetailsService;
import com.ludicamente.Ludicamente.config.JwtService;
import com.ludicamente.Ludicamente.config.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Value; // ¡Nueva importación!
import com.ludicamente.Ludicamente.auth.userdetails.CompositeUserDetailsService;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays; // ¡Nueva importación para Arrays.asList!
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CompositeUserDetailsService compositeUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Inyecta el valor de la propiedad 'cors.allowed-origins' desde application.properties
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    // Constructor con inyección de dependencias
    public SecurityConfig(CompositeUserDetailsService compositeUserDetailsService, JwtService jwtService, JwtAuthenticationFilter jwtAuthenticationFilter) {
    public SecurityConfig(
            CompositeUserDetailsService compositeUserDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.compositeUserDetailsService = compositeUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Proveedor de autenticación que utiliza nuestro servicio de usuarios personalizado
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(compositeUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Configura el administrador de autenticación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Filtro de seguridad principal
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/chatbot/**",
                                "/api/auth/**",
                                "/api/upload/image", // ¡Asegúrate de que este endpoint también sea permitido si no requiere autenticación inicial para la subida!
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/error",
                                "/favicon.ico",
                                "/resources/**",
                                "/api/categorias", // <-- AÑADIR ESTA LÍNEA
                                "/api/categorias/**" // <-- AÑADIR ESTA LÍNEA SI HAY SUB-RECURSOS (ej. /api/categorias/1)
                        "/api/chatbot/**",
                                "/api/files/upload",
                                "/api/servicios/categoria/**",
                                "/api/categorias",
                                "/api/pago/**"

                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider()) // <- Asegura que se use tu proveedor
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuración CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Divide la cadena de origins por comas y la convierte en una lista.
        // Esto permite múltiples orígenes definidos en application.properties.
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Ajusta según tu frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
