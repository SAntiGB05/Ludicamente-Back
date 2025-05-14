package com.ludicamente.Ludicamente.auth.userdetails;

import com.ludicamente.Ludicamente.model.Empleado;
import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class EmpleadoDetailsService implements UserDetailsService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoDetailsService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Empleado empleado = empleadoRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Empleado no encontrado con correo: " + username));

        return new EmpleadoUserDetails(empleado);
    }
}