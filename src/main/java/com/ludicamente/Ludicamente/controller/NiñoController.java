package com.ludicamente.Ludicamente.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/nino")
public class NiñoController {

    @GetMapping("/saludo")
    public String saludar(){
        return "holas que hacen";
    }
}
