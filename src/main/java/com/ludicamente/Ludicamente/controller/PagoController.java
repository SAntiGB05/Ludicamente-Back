package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.dto.DetallePagoDTO;
import com.ludicamente.Ludicamente.model.*;
import com.ludicamente.Ludicamente.service.DetalleFacService;
import com.ludicamente.Ludicamente.service.FacturaService;
import com.ludicamente.Ludicamente.service.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Date;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pago")
public class PagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private DetalleFacService detalleFacService;

    @PostMapping("/crear-preferencia")
    public ResponseEntity<String> crear(@RequestBody DetallePagoDTO dto) {
        String id = mercadoPagoService.crearPreferencia(dto);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> recibirWebhook(@RequestBody Map<String, Object> data) {
        if ("payment".equals(data.get("type"))) {
            Map<String, Object> inner = (Map<String, Object>) data.get("data");
            Long idPago = Long.valueOf(inner.get("id").toString());

            // Obtener estado del pago desde la API de MercadoPago
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth("TEST-1692463810288523-062616-ba718c667dcf92b0835c59a4c7d71a49-2521088002"); // Reemplaza por tu token real
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.mercadopago.com/v1/payments/" + idPago,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            Map<String, Object> pagoInfo = response.getBody();
            String estado = (String) pagoInfo.get("status");

            if ("approved".equals(estado)) {
                // Obtener datos desde external_reference
                String externalReference = (String) pagoInfo.get("external_reference");
                Map<String, String> referencias = Arrays.stream(externalReference.split("&"))
                        .map(s -> s.split("="))
                        .collect(Collectors.toMap(s -> s[0], s -> s[1]));

                Integer idNino = Integer.valueOf(referencias.get("nino"));
                Integer idEmpleado = Integer.valueOf(referencias.get("empleado"));
                Integer idServicio = Integer.valueOf(referencias.get("servicio"));

                // Crear factura
                Factura factura = new Factura();
                factura.setMetodoPago(Factura.MetodoPago.TARJETA);
                factura.setEstado(Factura.EstadoFactura.PAGADA);
                factura.setFecha(new java.util.Date());
                BigDecimal monto = BigDecimal.valueOf(((Number) pagoInfo.get("transaction_amount")).doubleValue());
                factura.setSubtotal(monto);
                factura.setDescuento(BigDecimal.ZERO);
                factura.setImpuestos(BigDecimal.ZERO); // puedes modificar si tienes lógica
                factura.setValorTotal(monto);
                factura.setNiño(new Niño(idNino));
                factura.setEmpleado(new Empleado(idEmpleado));

                Factura facturaGuardada = facturaService.crearFactura(factura);

                // Crear detalle
                DetalleFactura detalle = new DetalleFactura();
                detalle.setFactura(facturaGuardada);
                detalle.setServicio(new Servicio(idServicio));
                detalle.setCantidad(1);
                detalle.setPrecioUnitario(monto);
                detalle.setDescuentoUnitario(BigDecimal.ZERO);
                detalle.setHorario(new Time(System.currentTimeMillis()));
                detalle.setFecha(new Date(System.currentTimeMillis()));
                detalle.setObservaciones("Compra desde plataforma de pago");

                detalleFacService.crearDetalle(detalle);

                System.out.println("✅ Factura y detalle creados exitosamente.");
            }
        }

        return ResponseEntity.ok().build();
    }

}
