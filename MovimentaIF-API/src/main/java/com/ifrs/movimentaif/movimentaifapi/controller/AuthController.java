package com.ifrs.movimentaif.movimentaifapi.controller;

import com.google.firebase.auth.FirebaseToken;
import com.ifrs.movimentaif.movimentaifapi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Token vem no formato "Bearer <idToken>"
            String idToken = authorizationHeader.replace("Bearer ", "");

            FirebaseToken decodedToken = authService.verifyIdToken(idToken);

            // Aqui você poderia salvar o usuário no banco, se necessário
            if (!authService.isProfessor(decodedToken)) {
                return ResponseEntity.status(403).body("Acesso restrito: apenas professores e admins.");
            }

            return ResponseEntity.ok(decodedToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token inválido: " + e.getMessage());
        }
    }
}
