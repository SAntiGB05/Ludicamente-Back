package com.ludicamente.Ludicamente.config;
// Importaciones necesarias
import com.ludicamente.Ludicamente.auth.userdetails.CompositeUserDetailsService;
import com.ludicamente.Ludicamente.config.JwtService;
import com.ludicamente.Ludicamente.config.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value; // ¡Nueva importación!
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
    private final JwtService jwtService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

// Inyecta el valor de la propiedad 'cors.allowed-origins' desde application.properties
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

// Constructor con inyección de dependencias
    public SecurityConfig(CompositeUserDetailsService compositeUserDetailsService, JwtService jwtService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.compositeUserDetailsService = compositeUserDetailsService;
        this.jwtService = jwtService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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
                        .requestMatchers(
                                "/api/chatbot/**",
                                "/api/auth/**",
                                "/api/upload/image",
                                "/api/gallery/images",
                                "/api/gallery/hide-image",
                                "/api/gallery/show-image", // <-- ¡ASEGÚRATE DE ESTA LÍNEA!
                                "/api/gallery/hidden-images", // <-- ¡ASEGÚRATE DE ESTA LÍNEA!
                                "/v3/api-docs/**",
                                "/error",
                                "/favicon.ico",
                                "/resources/**",
                                "/api/categorias",
                                "/api/categorias/**",
                                "/swagger-ui/**", // <-- También es buena idea tener esto como público
                                "/swagger-resources/**" // <-- Y esto
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
// Divide la cadena de origins por comas y la convierte en una lista.
// Esto permite múltiples orígenes definidos en application.properties.
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
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