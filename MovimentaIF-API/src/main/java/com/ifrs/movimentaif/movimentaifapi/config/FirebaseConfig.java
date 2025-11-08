package com.ifrs.movimentaif.movimentaifapi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.admin.credentials}")
    private String serviceAccountJson;

    // Injeta a URL do properties
    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Bean
    public Firestore firestore() throws IOException {

        InputStream serviceAccount = new ByteArrayInputStream(
                serviceAccountJson.getBytes(StandardCharsets.UTF_8)
        );

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(databaseUrl) // Adiciona a URL do DB
                .build();

        FirebaseApp firebaseApp;

        // Lógica robusta para Hot Reload
        try {
            // Tenta pegar a instância [DEFAULT] que já pode existir
            firebaseApp = FirebaseApp.getInstance();
        } catch (IllegalStateException e) {
            // Se não existir, inicializa
            firebaseApp = FirebaseApp.initializeApp(options);
        }

        // Retorna o Firestore associado a essa app
        return FirestoreClient.getFirestore(firebaseApp);
    }
}

