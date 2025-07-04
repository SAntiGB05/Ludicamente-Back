package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.dto.DetalleFacDto;
import com.ludicamente.Ludicamente.dto.FacturaConDetallesDto;
import com.ludicamente.Ludicamente.dto.FacturaDto;
import com.ludicamente.Ludicamente.model.DetalleFactura;
import com.ludicamente.Ludicamente.model.Factura;
import com.ludicamente.Ludicamente.model.Servicio;
import com.ludicamente.Ludicamente.repository.*;
import com.ludicamente.Ludicamente.service.DetalleFacService;
import com.ludicamente.Ludicamente.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaServiceImpl implements FacturaService {
    @Autowired
    private NiñoRepository niñoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private DetalleFacRepository detalleFacRepository;
    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private DetalleFacService detalleFacService;

    @Override
    public Factura crearFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    @Override
    public List<Factura> listarFacturas() {
        return facturaRepository.findAll();
    }

    @Override
    public Optional<Factura> actualizarFactura(Integer id, Factura facturaActualizada) {
        Optional<Factura> facturaExistente = facturaRepository.findById(id);
        if (facturaExistente.isPresent()) {
            Factura factura = facturaExistente.get();
            factura.setFecha(facturaActualizada.getFecha());
            factura.setSubtotal(facturaActualizada.getSubtotal());
            factura.setImpuestos(facturaActualizada.getImpuestos());
            factura.setValorTotal(facturaActualizada.getValorTotal());
            factura.setEstado(facturaActualizada.getEstado());
            factura.setObservaciones(facturaActualizada.getObservaciones());
            factura.setNiño(facturaActualizada.getNiño());
            factura.setEmpleado(facturaActualizada.getEmpleado());
            return Optional.of(facturaRepository.save(factura));
        }
        return Optional.empty();
    }

    @Override
    public boolean eliminarFactura(Integer id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public FacturaDto convertirAFacturaDto(Factura factura) {
        FacturaDto dto = new FacturaDto();
        dto.setCodFactura(factura.getCodFactura());

        if (factura.getFecha() != null) {
            LocalDate localDate = factura.getFecha()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            dto.setFecha(localDate);
        }

        dto.setEstado(factura.getEstado().name());
        dto.setMetodoPago(factura.getMetodoPago().name());
        dto.setSubtotal(factura.getSubtotal());
        dto.setImpuestos(factura.getImpuestos());
        dto.setValorTotal(factura.getValorTotal());

        if (factura.getNiño() != null) {
            dto.setFkidNino(factura.getNiño().getIdNiño());
        }

        if (factura.getEmpleado() != null) {
            dto.setFkidEmpleado(factura.getEmpleado().getIdEmpleado());
        }

        return dto;
    }

    @Override
    public Optional<FacturaConDetallesDto> obtenerFacturaConDetallesDto(Integer codFactura) {
        try {
            Optional<Factura> facturaOpt = facturaRepository.findById(codFactura);
            if (facturaOpt.isEmpty()) {
                System.out.println("❌ Factura no encontrada con ID: " + codFactura);
                return Optional.empty();
            }

            Factura factura = facturaOpt.get();
            FacturaDto facturaDto = convertirAFacturaDto(factura);

            List<DetalleFacDto> detallesDto = detalleFacService.obtenerPorFactura(codFactura)
                    .stream()
                    .map(detalle -> {
                        DetalleFacDto dto = new DetalleFacDto();
                        dto.setCodDetalle(detalle.getCodDetalle());
                        dto.setCantidad(detalle.getCantidad());
                        dto.setPrecioUnitario(detalle.getPrecioUnitario());
                        dto.setDescuentoUnitario(detalle.getDescuentoUnitario());
                        dto.setFecha(detalle.getFecha().toString());
                        dto.setHora(detalle.getHorario().toString());
                        dto.setSubtotalItem(detalle.getSubtotalItem());
                        dto.setFactura(detalle.getFactura().getCodFactura());
                        dto.setServicio(detalle.getServicio().getCodServicio());
                        dto.setObservaciones(detalle.getObservaciones());

                        if (detalle.getFactura().getNiño() != null) {
                            dto.setNombreCliente(detalle.getFactura().getNiño().getNombre());
                            dto.setTelefonoCliente(detalle.getFactura().getNiño().getAcudiente().getTelefono());
                            dto.setEmailCliente(detalle.getFactura().getNiño().getAcudiente().getCorreo());
                        }

                        dto.setDescripcion(detalle.getServicio().getNombreServicio());
                        return dto;
                    })
                    .toList();

            System.out.println("✅ Factura encontrada: " + facturaDto.getCodFactura());
            System.out.println("📦 Detalles encontrados: " + detallesDto.size());

            return Optional.of(new FacturaConDetallesDto(facturaDto, detallesDto));
        } catch (Exception e) {
            System.err.println("🚨 Error al obtener factura con detalles: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }



    @Override
    public List<FacturaDto> obtenerFacturasDto() {
        return facturaRepository.findAll()
                .stream()
                .map(this::convertirAFacturaDto)
                .toList();
    }

}
