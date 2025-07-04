package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.DetalleFacDto;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @Value("${mercadopago.webhook.url}")
    private String webhookUrl;

    @Value("${frontend.url.success}")
    private String successUrl;

    @Value("${frontend.url.failure}")
    private String failureUrl;

    @Value("${frontend.url.pending}")
    private String pendingUrl;

    // Simula almacenamiento temporal de órdenes pendientes
    private final Map<String, DetalleFacDto> pendingOrders = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            System.out.println("🟢 Inicializando SDK MercadoPago con token: " + accessToken);
            MercadoPago.SDK.setAccessToken(accessToken);
        } catch (MPConfException e) {
            System.err.println("❌ Error al configurar MercadoPago: " + e.getMessage());
            throw new RuntimeException("Fallo en la configuración de MercadoPago", e);
        }
    }

    /**
     * Crea una preferencia de pago con Mercado Pago.
     *
     * @param detalle Detalle del servicio a pagar.
     * @return Mapa con el init_point y external_reference.
     * @throws MPException Si ocurre un error con la API.
     */
    public Map<String, String> crearPreferencia(DetalleFacDto detalle) throws MPException {
        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        if (detalle.getPrecioUnitario() == null) {
            throw new IllegalArgumentException("El precio unitario no puede ser nulo.");
        }

        // Generar referencia única
        String externalReference = UUID.randomUUID().toString();
        pendingOrders.put(externalReference, detalle);

        // Crear ítem
        Item item = new Item()
                .setTitle(detalle.getDescripcion() != null ? detalle.getDescripcion() : "Servicio")
                .setQuantity(detalle.getCantidad())
                .setUnitPrice(detalle.getPrecioUnitario().floatValue())
                .setDescription("Reserva de " + detalle.getDescripcion())
                .setCategoryId("services");

        // Crear pagador
        Payer payer = new Payer()
                .setEmail(detalle.getEmailCliente() != null
                        ? detalle.getEmailCliente()
                        : "santigbttobi@gmail.com");

        // URLs de redirección
        BackUrls backUrls = new BackUrls()
                .setSuccess(successUrl)
                .setFailure(failureUrl)
                .setPending(pendingUrl);

        // Crear preferencia
        Preference preference = new Preference()
                .appendItem(item)
                .setPayer(payer)
                .setExternalReference(externalReference)
                .setNotificationUrl(webhookUrl)
                .setBackUrls(backUrls)
                .setAutoReturn(Preference.AutoReturn.approved);

        // Guardar preferencia
        preference.save();
        String initPoint = preference.getInitPoint();

        if (initPoint == null || initPoint.isEmpty()) {
            System.err.println("❌ El init_point es null. Verifica si el token de acceso es válido o si los datos están correctos.");
            throw new IllegalStateException("El init_point devuelto por MercadoPago es null.");
        }

        System.out.println("✅ Preferencia creada. Init_point: " + initPoint);

        // Respuesta al frontend
        Map<String, String> response = new HashMap<>();
        response.put("init_point", initPoint);
        response.put("external_reference", externalReference);
        return response;
    }

    /**
     * Obtener detalles de pago por ID.
     */
    public Payment getPaymentDetails(String paymentId) throws MPException {
        return Payment.findById(paymentId);
    }

    /**
     * Obtener orden pendiente por referencia.
     */
    public DetalleFacDto getPendingOrderDetails(String externalReference) {
        return pendingOrders.get(externalReference);
    }

    /**
     * Eliminar orden pendiente luego del pago.
     */
    public void removePendingOrder(String externalReference) {
        pendingOrders.remove(externalReference);
    }
}
