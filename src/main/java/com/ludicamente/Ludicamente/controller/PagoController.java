package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.dto.DetalleFacDto;
import com.ludicamente.Ludicamente.model.Factura;
import com.ludicamente.Ludicamente.service.InvoiceCreationService;
import com.ludicamente.Ludicamente.service.MercadoPagoService;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    private final MercadoPagoService mercadoPagoService;
    private final InvoiceCreationService invoiceCreationService;

    public PagoController(MercadoPagoService mercadoPagoService, InvoiceCreationService invoiceCreationService) {
        this.mercadoPagoService = mercadoPagoService;
        this.invoiceCreationService = invoiceCreationService;
    }

    @PostMapping("/crear-preferencia")
    public ResponseEntity<Map<String, String>> crearPreferencia(@RequestBody DetalleFacDto request) throws Exception {
        Map<String, String> response = mercadoPagoService.crearPreferencia(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleMercadoPagoWebhook(
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "data.id", required = false) String dataId
    ) {
        String usadoTopic = topic != null ? topic : type;
        String usadoId = id != null ? id : dataId;

        System.out.println("Webhook recibido: Topic=" + usadoTopic + ", ID=" + usadoId);

        if ("payment".equalsIgnoreCase(usadoTopic)) {
            try {
                Payment payment = mercadoPagoService.getPaymentDetails(usadoId);
                if (payment != null) {
                    String statusStr = String.valueOf(payment.getStatus()).trim();
                    if ("approved".equalsIgnoreCase(statusStr)) {
                        String externalReference = payment.getExternalReference();
                        if (externalReference == null || externalReference.isEmpty()) {
                            System.err.println("External reference is missing for approved payment: " + payment.getId());
                            return ResponseEntity.badRequest().build();
                        }

                        DetalleFacDto originalDetalle = mercadoPagoService.getPendingOrderDetails(externalReference);
                        if (originalDetalle != null) {
                            Factura createdFactura = invoiceCreationService.createInvoiceFromPayment(payment, originalDetalle);
                            mercadoPagoService.removePendingOrder(externalReference);
                            return ResponseEntity.ok().build();
                        } else {
                            System.err.println("No se encontraron detalles para la referencia: " + externalReference);
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                        }
                    } else {
                        System.out.println("Pago recibido, pero no aprobado. Estado: " + statusStr);
                        return ResponseEntity.ok().build();
                    }
                } else {
                    System.out.println("No se encontró el pago con ID: " + usadoId);
                    return ResponseEntity.ok().build();
                }

            } catch (MPException e) {
                System.err.println("Error de Mercado Pago: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } catch (Exception e) {
                System.err.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return ResponseEntity.ok().build(); // Ignorar otros topics
    }
}
