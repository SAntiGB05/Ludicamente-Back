package com.ludicamente.Ludicamente.auth.userdetails;

import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.repository.AcudienteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AcudienteDetailsService implements UserDetailsService {

    private final AcudienteRepository acudienteRepository;

    public AcudienteDetailsService(AcudienteRepository acudienteRepository) {
        this.acudienteRepository = acudienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Acudiente acudiente = acudienteRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Acudiente no encontrado con correo: " + username));

        return new AcudienteUserDetails(acudiente);

    }
}
