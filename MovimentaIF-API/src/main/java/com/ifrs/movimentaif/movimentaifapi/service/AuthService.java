package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.ifrs.movimentaif.movimentaifapi.model.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User verifyTokenAndSaveUser(String idToken) throws Exception {
        // Verifica token vindo do front
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

        String uid = decodedToken.getUid();
        String name = decodedToken.getName();
        String email = decodedToken.getEmail();

        // Define role básico (aqui tu pode colocar regra: email/senha = STUDENT, Google = TEACHER)
        String role = "STUDENT";

        User user = new User(uid, name, email, role, System.currentTimeMillis());

        // Salva/atualiza no Firestore
        userService.saveUser(user);

        return user;
    }

    public String registerUser(String email, String uid, String role) {
        // só exemplo por enquanto
        User user = new User();
        user.setEmail(email);
        user.setUid(uid);
        user.setRole(role);
        user.setCreatedAt(System.currentTimeMillis());

        return "Usuário registrado com sucesso: " + user.getEmail();
    }
}
