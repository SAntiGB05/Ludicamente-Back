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
    public ResponseEntity<Void> handleMercadoPagoWebhook(@RequestParam("topic") String topic, @RequestParam("id") String id) {
        System.out.println("Webhook recibido: Topic=" + topic + ", ID=" + id);

        if (Objects.equals(topic, "payment")) {
            try {
                Payment payment = mercadoPagoService.getPaymentDetails(id);

                if (payment != null) {
                    String statusStr = String.valueOf(payment.getStatus()).trim();
                    System.out.println("Estado recibido (raw): " + statusStr + " - clase: " + payment.getStatus().getClass());

                    if ("approved".equalsIgnoreCase(statusStr)) {
                        System.out.println("Pago Aprobado: ID=" + payment.getId() + ", External Reference=" + payment.getExternalReference());

                        String externalReference = payment.getExternalReference();
                        if (externalReference == null || externalReference.isEmpty()) {
                            System.err.println("External reference is missing for approved payment: " + payment.getId());
                            return ResponseEntity.badRequest().build();
                        }

                        System.out.println("Buscando detalles para externalReference: " + externalReference);
                        DetalleFacDto originalDetalle = mercadoPagoService.getPendingOrderDetails(externalReference);

                        if (originalDetalle != null) {
                            Factura createdFactura = invoiceCreationService.createInvoiceFromPayment(payment, originalDetalle);
                            System.out.println("Factura y DetalleFactura creados para el pago ID: " + payment.getId());

                            mercadoPagoService.removePendingOrder(externalReference);
                            return ResponseEntity.ok().build();
                        } else {
                            System.err.println("No se encontraron detalles de orden pendientes para la referencia externa: " + externalReference);
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                        }
                    } else {
                        System.out.println("Pago recibido, pero no aprobado. Estado: " + statusStr);
                        return ResponseEntity.ok().build();
                    }
                } else {
                    System.out.println("No se encontró el pago con ID: " + id);
                    return ResponseEntity.ok().build();
                }

            } catch (MPException e) {
                System.err.println("Error de Mercado Pago al procesar webhook: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } catch (Exception e) {
                System.err.println("Error inesperado al procesar webhook: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return ResponseEntity.ok().build(); // Otros topics ignorados
    }
}
