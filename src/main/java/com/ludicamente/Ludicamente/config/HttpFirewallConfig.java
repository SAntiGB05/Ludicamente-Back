package com.ludicamente.Ludicamente.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
public class HttpFirewallConfig {

    @Bean
    public HttpFirewall allowSpecialCharactersHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        // ✅ Permite el uso de % en parámetros codificados, pero no permite otros carácteres peligrosos
        firewall.setAllowUrlEncodedPercent(true);
        return firewall;
    }

}
