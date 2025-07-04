// Archivo: src/main/java/com/ludicamente/Ludicamente/model/Empleado.java
// Descripción: Modelo de la entidad Empleado, mapeado a la tabla 'empleado' en la base de datos.

package com.ludicamente.Ludicamente.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // Para manejar relaciones bidireccionales en JSON
import com.fasterxml.jackson.annotation.JsonIgnore;       // Para ignorar la contraseña en la serialización
import com.fasterxml.jackson.annotation.JsonFormat;      // Para el formato de fecha en JSON
import jakarta.persistence.*; // Anotaciones JPA
import java.math.BigDecimal; // Para salario
import java.time.LocalDate;  // Para fechaContratacion
import java.util.List;
import java.util.Objects;    // Para equals/hashCode (buena práctica)


@Entity
@Table(name = "empleado") // Nombre exacto de la tabla en tu base de datos
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado") // Mapea a la columna 'id_empleado' en la BD
    private Integer idEmpleado; // Tipo Integer, consistente con tu DDL

    @Column(name = "cedula_empleado", nullable = false, length = 20, unique = true)
    private String cedulaEmpleado;

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "correo", nullable = false, length = 100, unique = true)
    private String correo;

    @JsonIgnore // Oculta la contraseña cuando se serializa el objeto a JSON
    @Column(name = "contraseña", nullable = false, length = 255)
    private String contraseña;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "direccion", length = 100)
    private String direccion;

    @Column(name = "fecha_contratacion", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd") // Para asegurar el formato correcto al serializar/deserializar JSON
    private LocalDate fechaContratacion;

    @Column(name = "salario", nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(name = "horario", length = 100)
    private String horario;

    @Column(name = "nivel_acceso", nullable = false)
    private Integer nivelAcceso;

    @Enumerated(EnumType.STRING) // Almacena el Enum como String ('activo', 'inactivo') en la BD
    @Column(name = "estado", nullable = false)
    private EstadoEmpleado estado = EstadoEmpleado.activo; // Valor por defecto si se desea

    // Define el Enum directamente dentro de la clase Empleado
    public enum EstadoEmpleado {
        activo,
        inactivo
    }

    // --- Relaciones con otras entidades ---
    // Asegúrate de que las clases Factura, Bitacora, Post existan en tu paquete model
    // y que tengan la relación @ManyToOne hacia Empleado.

    @JsonBackReference("empleado-facturas") // Nombre de referencia para evitar ciclos infinitos
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Factura> facturas;

    @JsonBackReference("empleado-bitacoras") // Nombre de referencia para evitar ciclos infinitos
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Bitacora> bitacoras;

    @JsonBackReference("empleado-posts") // Nombre de referencia para evitar ciclos infinitos
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;

    // --- Constructor vacío (necesario para JPA) ---
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
