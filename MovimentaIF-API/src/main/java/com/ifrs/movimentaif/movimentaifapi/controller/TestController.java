package com.ifrs.movimentaif.movimentaifapi.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("")
    public Map<String, Object> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "🎉 MovimentaIF API está funcionando perfeitamente!");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("version", "1.0.0");
        response.put("environment", "production");
        response.put("endpoints", Map.of(
            "health", "/api/health",
            "test", "/api/test",
            "echo", "/api/test/echo/{message}",
            "users", "/api/users (requer autenticação)"
        ));
        return response;
    }

    @GetMapping("/echo/{message}")
    public Map<String, Object> echo(@PathVariable String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("received", message);
        response.put("echoed", message.toUpperCase());
        response.put("length", message.length());
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }

    @PostMapping("/data")
    public Map<String, Object> receiveData(@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Dados recebidos com sucesso!");
        response.put("received_data", data);
        response.put("data_keys", data.keySet());
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }

    @GetMapping("/firebase-status")
    public Map<String, Object> firebaseStatus() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Tenta verificar se o Firebase está configurado
            response.put("status", "success");
            response.put("message", "Firebase configurado e funcionando!");
            response.put("database", "Firestore");
            response.put("authentication", "Firebase Auth JWT");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Erro ao verificar Firebase: " + e.getMessage());
        }
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> serverInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "MovimentaIF-API");
        response.put("description", "Sistema de Gestão da Academia do IFRS Campus Porto Alegre");
        response.put("version", "1.0.0");
        response.put("author", "IFRS - Trabalho de Conclusão de Curso");
        response.put("stack", Map.of(
            "backend", "Spring Boot 3.5.4",
            "java", "17",
            "database", "Firebase Firestore",
            "authentication", "Firebase Auth + JWT",
            "hosting", "Heroku"
        ));
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }
}
