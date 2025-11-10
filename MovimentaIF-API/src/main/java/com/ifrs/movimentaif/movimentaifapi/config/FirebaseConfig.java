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

    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Bean
    public Firestore firestore() throws IOException {

        // Converte a string JSON (vinda da Config Var) em um InputStream
        InputStream serviceAccount = new ByteArrayInputStream(
                serviceAccountJson.getBytes(StandardCharsets.UTF_8)
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