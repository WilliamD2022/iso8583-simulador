package br.com.iso8583;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Iso8583Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Iso8583Application.class);
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("server.port", "8081"); // porta padrão forçada
        app.setDefaultProperties(defaults);
        app.run(args);
    }
}
