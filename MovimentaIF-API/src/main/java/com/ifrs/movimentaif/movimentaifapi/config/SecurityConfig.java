package com.ifrs.movimentaif.movimentaifapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    // Removemos a injeção direta do filtro customizado
    // public SecurityConfig(FirebaseAuthFilter firebaseAuthFilter) {
    //     this.firebaseAuthFilter = firebaseAuthFilter;
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // REMOVEMOS: .addFilterBefore(firebaseAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth
                        // 1. Endpoints públicos (sem autenticação)
                        .requestMatchers("/api/health", "/api/", "/").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/api/init/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2. TODAS as outras rotas exigem autenticação.
                        .anyRequest().authenticated()
                )
                // OAuth2 Resource Server para validação de JWT do Firebase
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})
                )
                // Desabilitar autenticação para endpoints públicos
                .anonymous(anonymous -> {});

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite origens locais e do domínio em produção
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8000",
                "http://localhost:5500",
                "http://127.0.0.1:5500",
                "https://*.herokuapp.com",
                "https://movimentaif-admin.pages.dev", // Adicione seu domínio frontend aqui
                "https://movimentaif-api.herokuapp.com",
                "https://marloncm.github.io" // GitHub Pages
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*", "Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}