package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.DetalleFacDto;
import com.ludicamente.Ludicamente.model.*;
import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import com.ludicamente.Ludicamente.repository.NiñoRepository;
import com.ludicamente.Ludicamente.repository.ServicioRepository;
import com.mercadopago.resources.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
public class InvoiceCreationService {

    private final FacturaService facturaService;
    private final DetalleFacService detalleFacService;
    private final NiñoRepository niñoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ServicioRepository servicioRepository;

    @Autowired
    public InvoiceCreationService(
            FacturaService facturaService,
            DetalleFacService detalleFacService,
            NiñoRepository niñoRepository,
            EmpleadoRepository empleadoRepository,
            ServicioRepository servicioRepository
    ) {
        this.facturaService = facturaService;
        this.detalleFacService = detalleFacService;
        this.niñoRepository = niñoRepository;
        this.empleadoRepository = empleadoRepository;
        this.servicioRepository = servicioRepository;
    }

    /**
     * Crea una Factura y su respectivo DetalleFactura a partir de un pago exitoso.
     *
     * @param payment         Objeto de pago de MercadoPago.
     * @param originalDetalle Detalles originales del servicio usado para crear la preferencia.
     * @return La factura creada.
     */
    public Factura createInvoiceFromPayment(Payment payment, DetalleFacDto originalDetalle) {

        // === 1. Buscar el Niño asociado si viene en el DTO ===
        Niño niño = null;
        if (originalDetalle.getFactura() != null) {
            niño = niñoRepository.findById(originalDetalle.getFactura()).orElse(null);
            if (niño == null) {
                System.err.println("Niño con ID " + originalDetalle.getFactura() + " no encontrado.");
            }
        }

        // === 2. Obtener Empleado por defecto (ID 1) ===
        Empleado empleado = empleadoRepository.findById(1).orElse(null);
        if (empleado == null) {
            System.err.println("Empleado con ID 1 no encontrado.");
        }

        // === 3. Obtener Servicio ===
        Servicio servicio = null;
        if (originalDetalle.getServicio() != null) {
            Optional<Servicio> optionalServicio = servicioRepository.findById(originalDetalle.getServicio());
            if (optionalServicio.isPresent()) {
                servicio = optionalServicio.get();
            } else {
                System.err.println("Servicio con ID " + originalDetalle.getServicio() + " no encontrado.");
            }
        }

        // === 4. Crear la Factura ===
        Factura factura = new Factura();
        factura.setFecha(new java.util.Date());
        factura.setSubtotal(BigDecimal.valueOf(payment.getTransactionAmount()));
        factura.setImpuestos(BigDecimal.ZERO);
        factura.setValorTotal(BigDecimal.valueOf(payment.getTransactionAmount()));
        factura.setDescuento(originalDetalle.getDescuentoUnitario() != null
                ? originalDetalle.getDescuentoUnitario()
                : BigDecimal.ZERO);
        factura.setMetodoPago(Factura.MetodoPago.TARJETA);
        factura.setEstado(Factura.EstadoFactura.PAGADA);
        factura.setObservaciones("Pago procesado vía Mercado Pago. ID: " + payment.getId());
        factura.setNiño(niño);
        factura.setEmpleado(empleado);

        Factura savedFactura = facturaService.crearFactura(factura);
        System.out.println("✅ Factura creada con ID: " + savedFactura.getCodFactura());

        // === 5. Crear DetalleFactura ===
        DetalleFactura detalle = new DetalleFactura();
        detalle.setCantidad(originalDetalle.getCantidad());
        detalle.setPrecioUnitario(originalDetalle.getPrecioUnitario());
        detalle.setDescuentoUnitario(originalDetalle.getDescuentoUnitario() != null
                ? originalDetalle.getDescuentoUnitario()
                : BigDecimal.ZERO);

        // === Hora (java.sql.Time) ===
        Time horaSql;
        if (originalDetalle.getHora() != null && !originalDetalle.getHora().isEmpty()) {
            try {
                SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
                java.util.Date parsedHora = sdfHora.parse(originalDetalle.getHora());
                horaSql = new Time(parsedHora.getTime());
            } catch (ParseException e) {
                System.err.println("⚠️ Error al parsear la hora: " + e.getMessage());
                horaSql = new Time(System.currentTimeMillis());
            }
        } else {
            horaSql = new Time(System.currentTimeMillis());
        }
        detalle.setHorario(horaSql);

        // === Fecha (java.sql.Date) ===
        Date fechaSql;
        if (originalDetalle.getFecha() != null && !originalDetalle.getFecha().isEmpty()) {
            try {
                SimpleDateFormat sdfFecha = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = sdfFecha.parse(originalDetalle.getFecha());
                fechaSql = new Date(parsedDate.getTime());
            } catch (ParseException e) {
                System.err.println("⚠️ Error al parsear la fecha: " + e.getMessage());
                fechaSql = new Date(System.currentTimeMillis());
            }
        } else {
            fechaSql = new Date(System.currentTimeMillis());
        }
        detalle.setFecha(fechaSql);

        // === Subtotal ===
        BigDecimal subtotal = originalDetalle.getSubtotalItem();
        if (subtotal == null) {
            subtotal = originalDetalle.getPrecioUnitario().multiply(BigDecimal.valueOf(originalDetalle.getCantidad()));
        }

        // === Otros campos ===
        detalle.setObservaciones(originalDetalle.getObservaciones() != null
                ? originalDetalle.getObservaciones()
                : "Servicio pagado");

        detalle.setFactura(savedFactura);
        detalle.setServicio(servicio);

        DetalleFactura savedDetalle = detalleFacService.crearDetalle(detalle);
        System.out.println("✅ DetalleFactura creado con ID: " + savedDetalle.getCodDetalle());

        return savedFactura;
    }
}
