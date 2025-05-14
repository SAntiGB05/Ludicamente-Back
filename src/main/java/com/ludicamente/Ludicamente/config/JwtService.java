package com.ludicamente.Ludicamente.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final long expirationTime;
    private final Key signingKey;
    private final long refreshExpirationTime;

    public JwtService(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiration-time}") long expirationTime,
            @Value("${jwt.refresh-expiration-time}") long refreshExpirationTime) {

        if (secretKey == null || secretKey.length() < 32) {
            throw new IllegalArgumentException("La clave secreta debe tener al menos 32 caracteres");
        }

        this.expirationTime = expirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, expirationTime);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpirationTime);
    }

    private String buildToken(Map<String, Object> extraClaims,
                              UserDetails userDetails,
                              long expiration) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("authorities", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean usernameValid = username.equals(userDetails.getUsername());
            boolean tokenNotExpired = !isTokenExpired(token);

            if (!usernameValid) {
                logger.warn("El nombre de usuario en el token no coincide");
            }
            if (!tokenNotExpired) {
                logger.warn("El token ha expirado");
            }

            return usernameValid && tokenNotExpired;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.warn("Token expirado: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("Token no soportado: {}", e.getMessage());
            throw new JwtAuthenticationException("Token no soportado");
        } catch (MalformedJwtException e) {
            logger.error("Token malformado: {}", e.getMessage());
            throw new JwtAuthenticationException("Token malformado");
        } catch (SignatureException e) {
            logger.error("Firma inválida: {}", e.getMessage());
            throw new JwtAuthenticationException("Firma inválida");
        } catch (IllegalArgumentException e) {
            logger.error("Argumentos inválidos: {}", e.getMessage());
            throw new JwtAuthenticationException("Argumentos inválidos");
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = extractAllClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                ((List<String>) claims.get("authorities")).stream()
                        .map(authority -> (GrantedAuthority) () -> authority)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                authorities
        );
    }

    public static class JwtAuthenticationException extends RuntimeException {
        public JwtAuthenticationException(String message) {
            super(message);
        }
    }
}