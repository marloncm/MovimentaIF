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
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.admin.credentials}")
    private String serviceAccountJson;

    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Bean
    public Firestore firestore() throws IOException {

        // Decodifica Base64 se necessário, senão usa direto
        String jsonContent = serviceAccountJson;
        
        // Verifica se está em Base64 (sem { no início)
        if (!serviceAccountJson.trim().startsWith("{")) {
            byte[] decodedBytes = Base64.getDecoder().decode(serviceAccountJson);
            jsonContent = new String(decodedBytes, StandardCharsets.UTF_8);
        }
        
        // Converte a string JSON em um InputStream
        InputStream serviceAccount = new ByteArrayInputStream(
                jsonContent.getBytes(StandardCharsets.UTF_8)
        );

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(databaseUrl)
                .build();

        FirebaseApp firebaseApp;

        // Lógica robusta para Hot Reload (ou múltiplas inicializações)
        try {
            firebaseApp = FirebaseApp.getInstance();
        } catch (IllegalStateException e) {
            firebaseApp = FirebaseApp.initializeApp(options);
        }

        return FirestoreClient.getFirestore(firebaseApp);
    }
}