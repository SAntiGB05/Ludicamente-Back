package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.DetalleFacDto;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access.token}")
    private String accessToken;

    @PostConstruct
    public void init() {
        try {
            System.out.println("Token de MercadoPago: " + accessToken);
            MercadoPago.SDK.setAccessToken(accessToken);
        } catch (com.mercadopago.exceptions.MPConfException e) {
            System.err.println("Error al configurar MercadoPago: " + e.getMessage());
            throw new RuntimeException("Fallo en la configuración de MercadoPago", e);
        }
    }

    public String crearPreferencia(DetalleFacDto detalle) throws MPException {
        // Validaciones básicas
        if (detalle.getCantidad() == null || detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        if (detalle.getPrecioUnitario() == null) {
            throw new IllegalArgumentException("El precio unitario no puede ser nulo.");
        }

        // Crear item de la preferencia
        Item item = new Item()
                .setTitle(detalle.getDescripcion() != null ? detalle.getDescripcion() : "Servicio")
                .setQuantity(detalle.getCantidad())
                .setUnitPrice(detalle.getPrecioUnitario().floatValue())
                .setDescription("Reserva de " + detalle.getDescripcion())
                .setCategoryId("services");

        // Usuario de prueba (obligatorio en entorno sandbox con tarjetas de prueba)
        Payer payer = new Payer()
                .setEmail("test_user_123456@testuser.com"); // Usa un correo generado desde tu panel de usuarios de prueba

        // Crear preferencia
        Preference preference = new Preference()
                .appendItem(item)
                .setPayer(payer);

        // Guardar preferencia (llamada a Mercado Pago)
        preference.save();

        // Retornar URL para redirigir al cliente
        return preference.getInitPoint();
    }
}
