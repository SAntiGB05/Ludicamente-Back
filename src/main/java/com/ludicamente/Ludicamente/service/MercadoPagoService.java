package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.DetallePagoDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class MercadoPagoService {

    private static final String ACCESS_TOKEN = "TEST-1692463810288523-062616-ba718c667dcf92b0835c59a4c7d71a49-2521088002"; // ⚠️ Reemplázalo por tu token real

    public String crearPreferencia(DetallePagoDTO dto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Arma el item a pagar
        Map<String, Object> item = Map.of(
                "title", dto.getDescripcion(),
                "quantity", dto.getCantidad(),
                "unit_price", dto.getPrecio().floatValue()

        );

        // Construye el external_reference dinámicamente
        String externalReference = String.format(
                "nino=%d&empleado=%d&servicio=%d",
                dto.getIdNino(),
                dto.getIdEmpleado(),
                dto.getIdServicio()
        );

        // Arma el cuerpo completo de la preferencia
        Map<String, Object> body = Map.of(
                "items", List.of(item),
                "back_urls", Map.of(
                        "success", "http://localhost:5173/pago-exitoso",
                        "failure", "http://localhost:5173/pago-fallido"
                ),
                "auto_return", "approved",
                "external_reference", externalReference
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.mercadopago.com/checkout/preferences",
                request,
                Map.class
        );

        return (String) response.getBody().get("id");
    }
}
