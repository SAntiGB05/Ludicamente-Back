package com.ludicamente.Ludicamente.auth.userdetails;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CompositeUserDetailsService implements UserDetailsService {

    @Autowired
    private AcudienteDetailsService acudienteDetailsService;

    @Autowired
    private EmpleadoDetailsService empleadoDetailsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return empleadoDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // Log para depuración
            logger.debug("No se encontró acudiente con correo: {}", username);
            return acudienteDetailsService.loadUserByUsername(username);
        }
    }

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CompositeUserDetailsService.class);
}