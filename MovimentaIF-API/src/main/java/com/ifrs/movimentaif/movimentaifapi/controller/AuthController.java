package com.ifrs.movimentaif.movimentaifapi.controller;

import com.ifrs.movimentaif.movimentaifapi.model.User;
import com.ifrs.movimentaif.movimentaifapi.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public User login(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        // O token vem no formato "Bearer <idToken>"
        String idToken = authorizationHeader.replace("Bearer ", "");
        return authService.verifyTokenAndSaveUser(idToken);
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String uid, @RequestParam String role) {
        return authService.registerUser(email, uid, role);
    }
}
