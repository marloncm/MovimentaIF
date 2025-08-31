package com.ifrs.movimentaif.movimentaifapi.model;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String uid;      // ID do Firebase Auth
    private String name;
    private String email;
    private String role;     // "USER", "ADMIN"
}

