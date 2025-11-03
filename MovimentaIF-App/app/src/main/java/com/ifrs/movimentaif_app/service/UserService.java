package com.ifrs.movimentaif_app.service;

import android.util.Log;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.CompletableFuture;

/**
 * Serviço de comunicação com o backend Spring Boot para operações de usuário.
 * NOTA: Esta classe DEVE ser substituída pela sua implementação de rede real (Ex: Retrofit, Volley).
 */
public class UserService {

    private static final String TAG = "UserService";
    // CRÍTICO: 10.0.2.2 é o IP do localhost do seu computador no emulador Android
    private static final String API_BASE_URL = "http://10.0.2.2:8080/api";

    /**
     * Busca a role do usuário no backend.
     * @param userId O UID do Firebase.
     * @return CompletableFuture<String> contendo a role ("USER" ou "ADMIN").
     */
    public CompletableFuture<String> getUserRole(String userId) {
        // Implementação real exigiria uma chamada GET para /api/users/{userId}

        // MOCK: Para testar o fluxo de redirecionamento no frontend
        if (userId.length() > 20) {
            return CompletableFuture.completedFuture("USER");
        } else {
            return CompletableFuture.completedFuture("ADMIN");
        }
    }

    /**
     * Envia dados para o backend para criar ou atualizar o usuário.
     * Deve ser chamado APÓS o login social (Google).
     */
    public void upsertUser(FirebaseUser user, String providerId) {
        // Em uma aplicação real, você faria uma chamada POST /api/users
        // enviando o ID Token no cabeçalho de Autorização e os dados no corpo.

        Log.d(TAG, "UPSERT solicitado para o backend: UID=" + user.getUid() + ", Nome=" + user.getDisplayName());
        // Lógica de comunicação de rede real DEVE ser implementada aqui
    }
}
