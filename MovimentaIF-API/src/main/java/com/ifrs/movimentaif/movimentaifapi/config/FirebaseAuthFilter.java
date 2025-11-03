package com.ifrs.movimentaif.movimentaifapi.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority; // Importação necessária
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importação necessária
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class FirebaseAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // Permite que rotas PÚBLICAS passem sem token.
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = header.substring(7);

        try {
            // 1. Valida o token usando o Firebase Admin SDK
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();

            // 2. CRÍTICO: Cria a lista de permissões (Authorities).
            // Para rotas simples, uma lista vazia é o suficiente se você usar authenticated().
            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_USER") // Define um papel básico, necessário para o Spring
            );

            // 3. Cria o objeto de autenticação do Spring Security
            User principal = new User(uid, "", authorities);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, idToken, authorities);

            // 4. Define o usuário no contexto para que o Spring Security o reconheça
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (FirebaseAuthException e) {
            // Se a validação falhar, retorna 401.
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Firebase inválido ou expirado.");
        }
    }
}
