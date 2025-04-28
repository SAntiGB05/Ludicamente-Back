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

    @Column(length = 50, nullable = false)
    private String apellido;

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

    @Column(name = "fecha_nacimiento", nullable = false)
    private Date fechaNacimiento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(nullable = false, length = 100)
    private String horario;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_acceso", nullable = false, length = 15)
    private NivelAcceso nivelAcceso;

    @Column(length = 50)
    private String especialidad;

    @Column(length = 255)
    private String foto;

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

    // Constructor vacío
    public Empleado() {
    }

    // Constructor completo
    public Empleado(Integer idEmpleado, String cedula, String nombre, String apellido, String correo, String contraseña,
                    String telefono, String direccion, Date fechaContratacion, Date fechaNacimiento,
                    BigDecimal salario, String horario, NivelAcceso nivelAcceso, String especialidad, String foto, EstadoEmpleado estado) {
        this.idEmpleado = idEmpleado;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaContratacion = fechaContratacion;
        this.fechaNacimiento = fechaNacimiento;
        this.salario = salario;
        this.horario = horario;
        this.nivelAcceso = nivelAcceso;
        this.especialidad = especialidad;
        this.foto = foto;
        this.estado = estado;
    }

    // ======== Getters y Setters ========

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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public NivelAcceso getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(NivelAcceso nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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
        licencia,
        inactivo
    }

    public enum NivelAcceso {
        administrador,
        supervisor,
        empleado
    }
}