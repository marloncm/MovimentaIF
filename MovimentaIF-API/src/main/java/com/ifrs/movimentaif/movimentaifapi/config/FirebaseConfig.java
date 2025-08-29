package com.ifrs.movimentaif.movimentaifapi.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {


        @PostConstruct
        public void initialize() throws Exception {
            // tenta usar var de ambiente automaticamente; caso queira usar arquivo classpath,
            // substitua por getResourceAsStream("serviceAccountKey.json")
            InputStream serviceAccount = null;
            String env = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            if (env != null && !env.isEmpty()) {
                serviceAccount = new FileInputStream(env);
            } else {
                serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
    }

