// src/main/java/com/ludicamente/Ludicamente/config/JwtAuthenticationFilter.java
// (Asegúrate de que la ruta del paquete sea correcta, aquí se asume 'com.ludicamente.Ludicamente.config')
package com.ludicamente.Ludicamente.config;

import io.jsonwebtoken.JwtException; // Excepción para problemas con JWT
import jakarta.servlet.FilterChain; // Para pasar la petición al siguiente filtro
import jakarta.servlet.ServletException; // Excepción de Servlet
import jakarta.servlet.http.HttpServletRequest; // Objeto de petición HTTP
import jakarta.servlet.http.HttpServletResponse; // Objeto de respuesta HTTP
import org.slf4j.Logger; // Interfaz para logging
import org.slf4j.LoggerFactory; // Clase para obtener un logger
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Token de autenticación de Spring Security
import org.springframework.security.core.Authentication; // Interfaz de autenticación
import org.springframework.security.core.context.SecurityContextHolder; // Para gestionar el contexto de seguridad
import org.springframework.security.core.userdetails.UserDetails; // Interfaz para detalles del usuario
import org.springframework.security.core.userdetails.UserDetailsService; // Interfaz para cargar detalles del usuario
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Excepción cuando el usuario no es encontrado
import org.springframework.stereotype.Component; // <-- ¡AÑADIDA ESTA ANOTACIÓN CLAVE!
import org.springframework.util.AntPathMatcher; // Utilidad para coincidir patrones de ruta
import org.springframework.web.filter.OncePerRequestFilter; // Clase base para filtros que se ejecutan una vez por petición

import java.io.IOException; // Excepción de I/O
import java.util.List; // Utilidad para listas

/**
 * Filtro de autenticación JWT personalizado.
 * Se ejecuta una vez por cada petición para validar los tokens JWT
 * y configurar el contexto de seguridad de Spring.
 */
@Component // <-- ¡ESTA ANOTACIÓN ES CRÍTICA! Le dice a Spring que esta clase es un bean
// que debe ser gestionado y puede ser inyectado en otros componentes.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService; // Servicio para operaciones con JWT (creación, validación)
    private final UserDetailsService userDetailsService; // Servicio para cargar los detalles del usuario
    private final AntPathMatcher pathMatcher = new AntPathMatcher(); // Para comparar rutas con patrones comodín

    // Lista de endpoints públicos que no requieren autenticación JWT.
    // Esto es manejado también por SecurityConfig, pero tenerlo aquí permite un filtro más rápido.
    // **Importante: Este filtro debería coincidir con lo que tienes en SecurityConfig.
    // Aquí puedes añadir '/api/chatbot/**' si quieres que el filtro lo salte rápido antes que SecurityConfig.**
    private final List<String> publicEndpoints = List.of(
            "/api/auth/**",
            "/v3/api-docs/**",
            "/api/chatbot/**" // <-- Asegúrate de que esto esté aquí si quieres que el filtro lo omita
    );

    /**
     * Constructor para inyección de dependencias.
     * Spring inyectará automáticamente JwtService y tu UserDetailsService (CompositeUserDetailsService).
     *
     * @param jwtService Servicio para JWT.
     * @param userDetailsService Servicio para cargar detalles del usuario (será tu CompositeUserDetailsService).
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        logger.debug("UserDetailsService inyectado en JwtAuthenticationFilter: {}", userDetailsService.getClass().getName());
    }

    /**
     * Determina si este filtro debe aplicarse a la petición actual.
     * Se salta el filtro si la ruta de la petición coincide con alguno de los endpoints públicos.
     * @param request La petición HTTP.
     * @return true si el filtro NO debe aplicarse, false si SÍ debe aplicarse.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return publicEndpoints.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * Lógica principal del filtro JWT.
     * - Extrae el token JWT del encabezado de autorización.
     * - Si hay un token, intenta autenticar al usuario.
     * - Si no hay token o la autenticación falla, limpia el contexto de seguridad.
     * - Pasa la petición al siguiente filtro en la cadena.
     * @param request La petición HTTP.
     * @param response La respuesta HTTP.
     * @param filterChain La cadena de filtros.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Limpia el contexto de seguridad al inicio de cada petición para evitar autenticaciones residuales.
        SecurityContextHolder.clearContext();

        try {
            String token = extractTokenFromRequest(request);

            // Si se extrajo un token y no hay autenticación actual en el contexto de seguridad, intentar autenticar.
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateWithToken(token);
            }

            // Pasa la petición al siguiente filtro en la cadena.
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            // Captura excepciones relacionadas con JWT (token inválido, expirado, etc.)
            logger.warn("Autenticación JWT fallida (JwtException): {}", e.getMessage());
            SecurityContextHolder.clearContext(); // Asegura que el contexto esté limpio
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado: " + e.getMessage());
        } catch (UsernameNotFoundException e) {
            // Captura la excepción si el nombre de usuario extraído del token no se encuentra
            logger.warn("Autenticación JWT fallida (UsernameNotFoundException): {}", e.getMessage());
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario del token no encontrado: " + e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada durante el proceso de autenticación
            logger.error("Error inesperado durante la autenticación JWT: {}", e.getMessage(), e); // Log con stack trace
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno de autenticación."); // O SC_UNAUTHORIZED
        }
    }

    /**
     * Autentica a un usuario usando un token JWT.
     * @param token El token JWT.
     */
    private void authenticateWithToken(String token) {
        String username = jwtService.extractUsername(token);
        logger.debug("Nombre de usuario extraído del token: {}", username);

        // Carga los detalles del usuario usando el UserDetailsService inyectado.
        // Aquí userDetailsService será tu CompositeUserDetailsService.
        var userDetails = userDetailsService.loadUserByUsername(username);
        logger.debug("UserDetails cargados: {}, Roles: {}", userDetails.getUsername(), userDetails.getAuthorities());

        // Valida el token contra los detalles del usuario.
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new JwtException("Token JWT inválido o no corresponde al usuario.");
        }

        // Crea un objeto de autenticación y lo establece en el contexto de seguridad.
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, // Principal (el objeto UserDetails)
                null,        // Credenciales (no usamos la contraseña aquí para JWT)
                userDetails.getAuthorities() // Roles/autoridades del usuario
        );

        // Establece la autenticación en el contexto de seguridad de Spring.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.debug("Usuario autenticado exitosamente: {}", username);
    }

    /**
     * Extrae el token JWT del encabezado "Authorization" de la petición HTTP.
     * Espera el formato "Bearer <token>".
     * @param request La petición HTTP.
     * @return El token JWT como String, o null si no se encuentra o no tiene el formato correcto.
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Devuelve solo el token, sin "Bearer "
        }
        return null;
    }
}