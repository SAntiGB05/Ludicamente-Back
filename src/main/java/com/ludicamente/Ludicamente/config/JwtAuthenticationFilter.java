package com.ludicamente.Ludicamente.config;

import com.ludicamente.Ludicamente.auth.userdetails.AcudienteDetailsService;
import com.ludicamente.Ludicamente.auth.userdetails.EmpleadoDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final EmpleadoDetailsService empleadoDetailsService;
    private final AcudienteDetailsService acudienteDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Endpoints públicos que no requieren autenticación
    private final List<String> publicEndpoints = List.of(
            "/api/auth/**",
            "/v3/api-docs/**",
            "/api/chatbot/**",
            "/api/upload/image", // <-- ¡AÑADIR ESTA LÍNEA!
            "/error",             // <-- ¡AÑADIR ESTA LÍNEA!
            "/favicon.ico",       // <-- ¡AÑADIR ESTA LÍNEA!
            "/resources/**",       // <-- ¡AÑADIR ESTA LÍNEA!// <-- Asegúrate de que esto esté aquí si quieres que el filtro lo omita
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/api/chatbot/**",
            "/api/files/upload",
            "/api/servicios/categoria/**",
            "/api/categorias"
    );

    public JwtAuthenticationFilter(
            JwtService jwtService,
            EmpleadoDetailsService empleadoDetailsService,
            AcudienteDetailsService acudienteDetailsService) {
        this.jwtService = jwtService;
        this.empleadoDetailsService = empleadoDetailsService;
        this.acudienteDetailsService = acudienteDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return publicEndpoints.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        SecurityContextHolder.clearContext(); // Limpia contexto de autenticación previa

        try {
            String token = extractTokenFromRequest(request);

            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateWithToken(token);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            logger.warn("Autenticación JWT fallida (JwtException): {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado.");
        } catch (UsernameNotFoundException e) {
            logger.warn("Usuario no encontrado: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado.");
        } catch (Exception e) {
            logger.error("Error inesperado en filtro JWT: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno de autenticación.");
        }
    }

    private void authenticateWithToken(String token) {
        String username = jwtService.extractUsername(token);
        String rol = jwtService.extractClaim(token, claims -> claims.get("rol", String.class));

        logger.debug("Nombre extraído del token: {}", username);
        logger.debug("Rol extraído del token: {}", rol);

        UserDetails userDetails;

        // Seleccionar el servicio adecuado según el rol
        if ("ACUDIENTE".equalsIgnoreCase(rol)) {
            userDetails = acudienteDetailsService.loadUserByUsername(username);
        } else if ("EMPLEADO".equalsIgnoreCase(rol)) {
            userDetails = empleadoDetailsService.loadUserByUsername(username);
        } else {
            throw new JwtException("Rol no reconocido en el token.");
        }

        // Validar el token
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new JwtException("Token JWT inválido o no corresponde al usuario.");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.debug("Usuario autenticado correctamente: {}", username);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
