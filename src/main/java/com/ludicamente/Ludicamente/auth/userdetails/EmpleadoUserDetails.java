package com.ludicamente.Ludicamente.auth.userdetails;

import com.ludicamente.Ludicamente.model.Empleado;
import com.ludicamente.Ludicamente.model.Empleado.EstadoEmpleado;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class EmpleadoUserDetails implements UserDetails {

    private final Empleado empleado;

    public EmpleadoUserDetails(Empleado empleado) {
        this.empleado = empleado;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convierte el nivelAcceso a un rol (ej: "ROLE_ADMIN", "ROLE_STAFF")
        String role = "ROLE_" + (empleado.getNivelAcceso() == 1 ? "ADMIN" : "STAFF");
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return empleado.getContraseña();
    }

    @Override
    public String getUsername() {
        return empleado.getCorreo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return empleado.getEstado() == EstadoEmpleado.activo;
    }

    // Método adicional para acceder al empleado completo
    public Empleado getEmpleado() {
        return empleado;
    }
}