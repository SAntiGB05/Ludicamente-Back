package com.ludicamente.Ludicamente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat; // Importación para el formato de fecha
import com.fasterxml.jackson.annotation.JsonProperty; // Opcional, para control de campos en JSON (e.g., contraseña)

import java.math.BigDecimal; // Para el salario
import java.time.LocalDate;  // Para la fecha de contratación

public class EmpleadoDto {

    // El ID se incluye si este DTO se usará para operaciones de actualización (PUT),
    // donde la ID se envía en la URL o en el cuerpo. Para creación (POST) no es necesario.
    private Integer idEmpleado;

    @NotBlank(message = "La cédula es obligatoria")
    // Considera añadir un @Size o @Pattern si tienes un formato específico para la cédula.
    // Ejemplo: @Size(min = 7, max = 20, message = "La cédula debe tener entre 7 y 20 caracteres")
    private String cedulaEmpleado;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres") // Basado en tu DDL
    private String nombre;

    @Email(message = "El correo electrónico no es válido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres") // Basado en tu DDL
    private String correo;

    // La contraseña solo es obligatoria al CREAR un empleado.
    // Para ACTUALIZAR, si se envía, debería ser a través de un DTO de actualización específico
    // o un endpoint separado de cambio de contraseña, y sin @NotBlank si es opcional.
    @NotBlank(message = "La contraseña es obligatoria") // Requerida para creación
    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres") // Basado en tu DDL
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Evita que la contraseña se muestre en respuestas GET
    private String contraseña;

    // Tu DDL indica que 'telefono' no es NOT NULL, pero tu @NotBlank lo hace obligatorio.
    // Si realmente es opcional en la BD y en el negocio, quita @NotBlank.
    // Añadimos @Pattern para la validación del formato (7 a 15 dígitos) aquí, donde debe ir.
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{7,15}", message = "El teléfono debe contener entre 7 y 15 dígitos numéricos")
    @Size(max = 15, message = "El teléfono no puede exceder los 15 caracteres") // Basado en tu DDL
    private String telefono;

    // Tu DDL indica que 'direccion' no es NOT NULL. Si es opcional, quita @NotBlank.
    @NotBlank(message = "La dirección es obligatoria") // Si la dirección es obligatoria
    @Size(max = 100, message = "La dirección no puede exceder los 100 caracteres") // Basado en tu DDL
    private String direccion;

    @NotNull(message = "La fecha de contratación es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd") // ¡IMPORTANTE! Asegura que la fecha se formatee como "YYYY-MM-DD"
    private LocalDate fechaContratacion; // Tipo correcto: LocalDate

    @NotNull(message = "El salario es obligatorio")
    @Min(value = 0, message = "El salario no puede ser negativo") // Asegura que el salario no sea negativo
    private BigDecimal salario; // Tipo correcto: BigDecimal

    // Tu DDL indica que 'horario' no es NOT NULL. Si es opcional, quita @NotBlank.
    @NotBlank(message = "El horario es obligatorio") // Si el horario es obligatorio
    @Size(max = 100, message = "El horario no puede exceder los 100 caracteres") // Basado en tu DDL
    private String horario;

    @NotNull(message = "El nivel de acceso es obligatorio")
    @Min(value = 1, message = "El nivel de acceso debe ser 1 o 2")
    @Max(value = 2, message = "El nivel de acceso debe ser 1 o 2")
    private Integer nivelAcceso;

    // Para el ENUM, el frontend enviará un String. La validación asegura que sea 'activo' o 'inactivo'.
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "activo|inactivo", message = "El estado debe ser 'activo' o 'inactivo'")
    private String estado;


    // Constructor vacío (necesario para la deserialización de JSON)
    public EmpleadoDto() {}

    // Constructor con parámetros (útil para pruebas o mapeo manual)
    public EmpleadoDto(Integer idEmpleado, String cedulaEmpleado, String nombre, String correo,
                       String contraseña, String telefono, String direccion, LocalDate fechaContratacion,
                       BigDecimal salario, String horario, Integer nivelAcceso, String estado) {
        this.idEmpleado = idEmpleado;
        this.cedulaEmpleado = cedulaEmpleado;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña; // Incluir contraseña en este constructor
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
        this.horario = horario;
        this.nivelAcceso = nivelAcceso;
        this.estado = estado;
    }

    // --- Getters y Setters ---
    // Asegúrate de que estos existen para todas las propiedades.
    // Puedes generarlos automáticamente en tu IDE (Alt+Insert en IntelliJ, botón derecho > Source > Generate Getters and Setters en Eclipse).

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

    public String getContraseña() { // Getter para la contraseña
        return contraseña;
    }

    public void setContraseña(String contraseña) { // Setter para la contraseña
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}