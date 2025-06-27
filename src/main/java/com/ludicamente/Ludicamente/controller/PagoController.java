package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.dto.DetalleFacDto;
import com.ludicamente.Ludicamente.service.MercadoPagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*") // Puedes limitarlo según tu frontend
public class PagoController {

    private final MercadoPagoService mercadoPagoService;

    public PagoController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping("/crear-preferencia")
    public ResponseEntity<Map<String, String>> crearPreferencia(@RequestBody DetalleFacDto request) throws Exception {
        String url = mercadoPagoService.crearPreferencia(request);
        Map<String, String> response = new HashMap<>();
        response.put("init_point", url);
        return ResponseEntity.ok(response);
    }

}
