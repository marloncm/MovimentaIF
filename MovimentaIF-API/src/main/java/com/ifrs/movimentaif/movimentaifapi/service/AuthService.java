package com.ifrs.movimentaif.movimentaifapi.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    /**
     * Verifica o token JWT enviado pelo frontend e retorna o FirebaseToken validado
     *
     * @param idToken token JWT do Google/Firebase enviado pelo frontend
     * @return FirebaseToken se válido
     * @throws Exception se o token for inválido ou expirado
     */
    public FirebaseToken verifyIdToken(String idToken) throws Exception {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    /**
     * Verifica se o usuário autenticado tem permissão de professor/admin
     * (pode ser baseado em roles do banco ou em custom claims do Firebase)
     */
    public boolean isProfessor(FirebaseToken token) {
        // Exemplo usando custom claims configurados no Firebase
        Object role = token.getClaims().get("role");
        return role != null && (role.equals("professor") || role.equals("admin"));
    }
}
