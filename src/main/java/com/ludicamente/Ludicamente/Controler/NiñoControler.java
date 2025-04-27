package com.ludicamente.Ludicamente.Controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/nino")
public class NiñoControler {

    @GetMapping("/saludo")
    public String saludar(){
        return "holas que hacen";
    }
}
