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

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CompositeUserDetailsService.class);

    @Autowired
    private EmpleadoDetailsService empleadoDetailsService;

    @Autowired
    private AcudienteDetailsService acudienteDetailsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return empleadoDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e1) {
            logger.debug("No se encontró empleado con correo: {}", username);
            try {
                return acudienteDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e2) {
                logger.debug("No se encontró acudiente con correo: {}", username);
                throw new UsernameNotFoundException("Usuario no encontrado con correo: " + username);
            }
        }
    }
}
