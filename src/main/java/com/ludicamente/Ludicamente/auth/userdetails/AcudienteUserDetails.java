package com.ludicamente.Ludicamente.auth.userdetails;

import com.ludicamente.Ludicamente.model.Acudiente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AcudienteUserDetails implements UserDetails {

    private final Acudiente acudiente;

    public AcudienteUserDetails(Acudiente acudiente) {
        this.acudiente = acudiente;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ACUDIENTE"));
    }

    @Override
    public String getPassword() {
        return acudiente.getContraseña();
    }

    @Override
    public String getUsername() {
        return acudiente.getCorreo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Puedes personalizar según tu lógica de negocio
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Puedes personalizar según tu lógica de negocio
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Puedes personalizar según tu lógica de negocio
    }

    @Override
    public boolean isEnabled() {
        return true; // Puedes verificar si el acudiente está activo
    }

    // Método adicional para obtener el acudiente completo si es necesario
    public Acudiente getAcudiente() {
        return this.acudiente;
    }
}