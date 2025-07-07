package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.auth.passwordReset.EmailServiceEmpleados; // Importa tu EmailService
import com.ludicamente.Ludicamente.model.Empleado;
import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import com.ludicamente.Ludicamente.service.EmpleadoService;
import jakarta.mail.MessagingException; // Importa MessagingException para manejar errores de correo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Importa PasswordEncoder
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID; // Para generar contraseñas aleatorias simples

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyecta PasswordEncoder

    @Autowired
    private EmailServiceEmpleados emailServiceEmpleados; // Inyecta tu EmailService

    // 1. Crear un empleado
    @Override
    public Empleado crearEmpleado(Empleado empleado) {
        // *** INICIO DE LOS CAMBIOS PARA ENVIAR CORREO DE BIENVENIDA ***

        // 1. Generar una contraseña temporal para el nuevo empleado
        // Importante: En un entorno de producción, considera una lógica de generación de contraseña más robusta.
        String contrasenaTemporal = generateRandomPassword();
        empleado.setContraseña(passwordEncoder.encode(contrasenaTemporal)); // Encripta la contraseña antes de guardarla

        // 2. Guardar el empleado en la base de datos (con la contraseña encriptada)
        Empleado empleadoGuardado = empleadoRepository.save(empleado);

        // 3. Enviar el correo de bienvenida con el correo y la contraseña temporal
        try {
            emailServiceEmpleados.enviarCorreoBienvenidaEmpleado(
                    empleadoGuardado.getCorreo(),  // Destinatario
                    empleadoGuardado.getNombre(),  // Nombre del empleado para el saludo
                    empleadoGuardado.getCorreo(),  // Correo de acceso
                    contrasenaTemporal             // Contraseña temporal (sin encriptar para el correo)
            );
            System.out.println("Correo de bienvenida enviado a: " + empleadoGuardado.getCorreo());
        } catch (MessagingException e) {
            // Si hay un error al enviar el correo, lo registramos.
            // La creación del empleado no debería fallar solo por un error de correo.
            System.err.println("Error al enviar el correo de bienvenida para el empleado " + empleadoGuardado.getCorreo() + ": " + e.getMessage());
            // Puedes considerar otras acciones aquí, como guardar el error en una tabla de logs
            // o intentar reenviar el correo más tarde (con una cola de mensajes, por ejemplo).
        }

        return empleadoGuardado;
        // *** FIN DE LOS CAMBIOS PARA ENVIAR CORREO DE BIENVENIDA ***
    }

    // Método auxiliar para generar una contraseña aleatoria simple
    // Considera mover esto a una utilidad o un servicio de seguridad si lo usas en más lugares.
    private String generateRandomPassword() {
        // Genera una cadena UUID y toma los primeros 8 caracteres como contraseña temporal.
        // Esto es simple, pero puedes hacerlo más robusto si lo necesitas (e.g., incluir caracteres especiales, números, letras mayúsculas/minúsculas).
        return UUID.randomUUID().toString().substring(0, 8);
    }


    // 2. Listar todos los empleados (sin cambios)
    @Override
    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    // 3. Actualizar un empleado (se mantiene la lógica, pero deberías considerar
    // si la contraseña también se puede actualizar aquí y cómo manejarlo)
    @Override
    public Optional<Empleado> actualizarEmpleado(Integer id, Empleado empleadoActualizado) {
        Optional<Empleado> empleadoExistente = empleadoRepository.findById(id);
        if (empleadoExistente.isPresent()) {
            Empleado empleado = empleadoExistente.get();
            empleado.setCedulaEmpleado(empleadoActualizado.getCedulaEmpleado());
            empleado.setNombre(empleadoActualizado.getNombre());
            empleado.setCorreo(empleadoActualizado.getCorreo());
            empleado.setTelefono(empleadoActualizado.getTelefono());
            empleado.setDireccion(empleadoActualizado.getDireccion());
            empleado.setSalario(empleadoActualizado.getSalario());
            empleado.setNivelAcceso(empleadoActualizado.getNivelAcceso());
            empleado.setEstado(empleadoActualizado.getEstado());
            empleado.setHorario(empleadoActualizado.getHorario());
            empleado.setFechaContratacion(empleadoActualizado.getFechaContratacion());
            // Nota: Aquí NO estamos tocando la contraseña. Si necesitas actualizarla,
            // deberías tener un método específico para el cambio de contraseña.
            return Optional.of(empleadoRepository.save(empleado));
        }
        return Optional.empty();
    }

    // 4. Eliminar un empleado (sin cambios)
    @Override
    public boolean eliminarEmpleado(Integer id) {
        if (empleadoRepository.existsById(id)) {
            empleadoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Empleado obtenerEmpleadoPorCorreo(String correo) {
        return empleadoRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con correo: " + correo));
    }

    @Override
    public Optional<Empleado> obtenerEmpleadoPorId(Integer id) {
        return empleadoRepository.findById(id);
    }
}