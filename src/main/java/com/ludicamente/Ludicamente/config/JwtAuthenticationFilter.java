package com.ludicamente.Ludicamente.config;

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
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final List<String> publicEndpoints = List.of(
            "/api/auth/**",
            "/api/empleados/**",
            "/api/acudiente/**",
            "/api/servicios/**",
            "/v3/api-docs/**"
    );

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return publicEndpoints.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        SecurityContextHolder.clearContext(); // Limpia el contexto en cada request

        try {
            String token = extractTokenFromRequest(request);

            if (token != null) {
                authenticateWithToken(token);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException | UsernameNotFoundException e) {
            handleAuthenticationError(response, "Invalid authentication: " + e.getMessage());
        } catch (Exception e) {
            handleAuthenticationError(response, "Authentication failed: " + e.getMessage());
        }
    }

    private void authenticateWithToken(String token) {
        String username = jwtService.extractUsername(token);
        var userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new JwtException("Invalid token");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.debug("Authenticated user: {}", username);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void handleAuthenticationError(HttpServletResponse response, String message) throws IOException {
        logger.warn(message);
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
