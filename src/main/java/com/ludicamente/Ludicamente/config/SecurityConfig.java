package com.ludicamente.Ludicamente.config;

import com.ludicamente.Ludicamente.auth.userdetails.CompositeUserDetailsService;
import com.ludicamente.Ludicamente.config.JwtService;
import com.ludicamente.Ludicamente.config.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Importación para HttpMethod
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CompositeUserDetailsService compositeUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    public SecurityConfig(CompositeUserDetailsService compositeUserDetailsService, JwtService jwtService, JwtAuthenticationFilter jwtAuthenticationFilter) {
    public SecurityConfig(
            CompositeUserDetailsService compositeUserDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.compositeUserDetailsService = compositeUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Permitir todas las solicitudes OPTIONS (preflight requests)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // <-- ¡Esta es la línea clave!
                        .requestMatchers(
                                "/api/chatbot/**",
                                "/api/auth/**",
                                "/api/upload/image",
                                "/api/gallery/images",
                                "/api/gallery/hide-image",
                                "/api/gallery/show-image",
                                "/api/gallery/hidden-images",
                                "/v3/api-docs/**",
                                "/error",
                                "/favicon.ico",
                                "/resources/**",
                                "/api/categorias",
                                "/api/categorias/**",
                                "/swagger-ui/**",
                                "/swagger-resources"
=======
                                "/api/auth/**", "/api/upload/image", "/api/gallery/**",
                                "/api/chatbot/**", "/api/files/upload", "/api/servicios/categoria/**",
                                "/api/categorias/**", "/api/pago/**",
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**",
                                "/error", "/favicon.ico", "/resources/**",
                                "/api/acudiente/contar",
                                "/api/niños/contar-por-genero"
                                "/error", "/favicon.ico", "/resources/**","/api/pagos/crear-preferencia", "/api/pagos/webhook"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Asegúrate de que PATCH también esté permitido
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        CorsConfiguration config = new CorsConfiguration();
        // soporta múltiples orígenes separados por coma
        config.setAllowedOriginPatterns(List.of(allowedOrigins.split(",")));
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
