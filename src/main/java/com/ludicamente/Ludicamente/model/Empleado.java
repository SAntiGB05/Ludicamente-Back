package com.ludicamente.Ludicamente.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;


@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "cedula_empleado", nullable = false, length = 20, unique = true)
    private String cedula;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(nullable = false, length = 100, unique = true)
    private String correo;

    @Column(nullable = false, length = 255)
    private String contraseña;

    @Pattern(regexp = "\\d{7,15}")
    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(nullable = false, length = 100)
    private String direccion;

    @Column(name = "fecha_contratacion", nullable = false)
    private Date fechaContratacion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(nullable = false, length = 100)
    private String horario;

    @Column(name = "nivel_acceso", nullable = false, length = 15)
    private Integer nivelAcceso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoEmpleado estado = EstadoEmpleado.activo;

    // Relación con Facturas
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Factura> facturas;

    // Relación con Bitácora
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bitacora> bitacoras;

    // Relación con Post
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;

    // ... (el resto de tus imports)

    public Empleado(){

    }

        // Constructor privado para el Builder
        private Empleado(Builder builder) {
            this.idEmpleado = builder.idEmpleado;
            this.cedula = builder.cedula;
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

        // Método estático para crear el Builder
        public static Builder builder() {
            return new Builder();
        }

        // Clase Builder interna
        public static final class Builder {
            private Integer idEmpleado;
            private String cedula;
            private String nombre;
            private String correo;
            private String contraseña;
            private String telefono;
            private String direccion;
            private Date fechaContratacion;
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

            public Builder cedula(String cedula) {
                this.cedula = cedula;
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

            public Builder fechaContratacion(Date fechaContratacion) {
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

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
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

    public @Pattern(regexp = "\\d{7,15}") String getTelefono() {
        return telefono;
    }

    public void setTelefono(@Pattern(regexp = "\\d{7,15}") String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
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

    public enum EstadoEmpleado {
        activo,
        inactivo
    }

}