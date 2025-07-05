// Archivo: src/main/java/com/ludicamente/Ludicamente/model/Empleado.java

package com.ludicamente.Ludicamente.model;

// CAMBIAR A ESTA IMPORTACIÓN:
import com.fasterxml.jackson.annotation.JsonManagedReference; // Para manejar relaciones bidireccionales en JSON

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "cedula_empleado", nullable = false, length = 20, unique = true)
    private String cedulaEmpleado;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "correo", nullable = false, length = 100, unique = true)
    private String correo;

    @JsonIgnore
    @Column(name = "contraseña", nullable = false, length = 255)
    private String contraseña;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @Column(name = "fecha_contratacion", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaContratacion;

    @Column(name = "salario", nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(name = "horario", length = 100)
    private String horario;

    @Column(name = "nivel_acceso", nullable = false)
    private Integer nivelAcceso;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoEmpleado estado = EstadoEmpleado.activo;

    public enum EstadoEmpleado {
        activo,
        inactivo
    }

    // --- Relaciones con otras entidades ---
    // ¡CORREGIDO: DEBE SER @JsonManagedReference en el lado OneToMany!

    @JsonManagedReference("empleado-facturas") // <-- CORREGIDO
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Factura> facturas;

    @JsonManagedReference("empleado-bitacoras") // <-- CORREGIDO
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bitacora> bitacoras;

    @JsonManagedReference("empleado-posts") // <-- CORREGIDO
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;

    // --- Resto del código de la clase Empleado (constructores, builder, getters y setters) ---
    // ... (sin cambios en el resto del código que no sean las anotaciones de Jackson en las relaciones)
    // Asegúrate de que el resto de tu clase Empleado sea el que tienes.
    public Empleado(){
    }

    // Constructor para inicializar solo el ID (útil para referencias)
    public Empleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    // --- Constructor privado para el Builder ---
    private Empleado(Builder builder) {
        this.idEmpleado = builder.idEmpleado;
        this.cedulaEmpleado = builder.cedulaEmpleado;
        this.nombre = builder.nombre;
        this.correo = builder.correo;
        this.contraseña = builder.contraseña;
        this.telefono = builder.telefono;
        this.direccion = builder.direccion;
        this.fechaContratacion = builder.fechaContratacion;
        this.salario = builder.salario;
        this.horario = builder.horario;
        this.nivelAcceso = builder.nivelAcceso;
        this.estado = builder.estado;
        this.facturas = builder.facturas;
        this.bitacoras = builder.bitacoras;
        this.posts = builder.posts;
    }

    // --- Método estático para crear el Builder ---
    public static Builder builder() {
        return new Builder();
    }

    // --- Clase Builder interna ---
    public static final class Builder {
        private Integer idEmpleado;
        private String cedulaEmpleado;
        private String nombre;
        private String correo;
        private String contraseña;
        private String telefono;
        private String direccion;
        private LocalDate fechaContratacion;
        private BigDecimal salario;
        private String horario;
        private Integer nivelAcceso;
        private EstadoEmpleado estado = EstadoEmpleado.activo;
        private List<Factura> facturas;
        private List<Bitacora> bitacoras;
        private List<Post> posts;

        private Builder() {}

        public Builder idEmpleado(Integer idEmpleado) {
            this.idEmpleado = idEmpleado;
            return this;
        }

        public Builder cedulaEmpleado(String cedulaEmpleado) {
            this.cedulaEmpleado = cedulaEmpleado;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder correo(String correo) {
            this.correo = correo;
            return this;
        }

        public Builder contraseña(String contraseña) {
            this.contraseña = contraseña;
            return this;
        }

        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Builder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Builder fechaContratacion(LocalDate fechaContratacion) {
            this.fechaContratacion = fechaContratacion;
            return this;
        }

        public Builder salario(BigDecimal salario) {
            this.salario = salario;
            return this;
        }

        public Builder horario(String horario) {
            this.horario = horario;
            return this;
        }

        public Builder nivelAcceso(Integer nivelAcceso) {
            this.nivelAcceso = nivelAcceso;
            return this;
        }

        public Builder estado(EstadoEmpleado estado) {
            this.estado = estado;
            return this;
        }

        public Builder facturas(List<Factura> facturas) {
            this.facturas = facturas;
            return this;
        }

        public Builder bitacoras(List<Bitacora> bitacoras) {
            this.bitacoras = bitacoras;
            return this;
        }

        public Builder posts(List<Post> posts) {
            this.posts = posts;
            return this;
        }

        public Empleado build() {
            return new Empleado(this);
        }
    }

    // --- Getters y Setters ---
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCedulaEmpleado() {
        return cedulaEmpleado;
    }

    public void setCedulaEmpleado(String cedulaEmpleado) {
        this.cedulaEmpleado = cedulaEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Integer getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(Integer nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public EstadoEmpleado getEstado() {
        return estado;
    }

    public void setEstado(EstadoEmpleado estado) {
        this.estado = estado;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public List<Bitacora> getBitacoras() {
        return bitacoras;
    }

    public void setBitacoras(List<Bitacora> bitacoras) {
        this.bitacoras = bitacoras;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    // (Opcional) Métodos equals, hashCode y toString para depuración y buenas prácticas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empleado empleado = (Empleado) o;
        return Objects.equals(idEmpleado, empleado.idEmpleado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmpleado);
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "idEmpleado=" + idEmpleado +
                ", cedulaEmpleado='" + cedulaEmpleado + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fechaContratacion=" + fechaContratacion +
                ", salario=" + salario +
                ", horario='" + horario + '\'' +
                ", nivelAcceso=" + nivelAcceso +
                ", estado=" + estado +
                '}';
    }
}