package com.ifrs.movimentaif.movimentaifapi.model;

import lombok.Data;

@Data
public class User {
    private String uid;
    private String name;
    private String email;
    private String role; // STUDENT, TEACHER, ADMIN
    private long createdAt;

    public User(){}

    public User(String uid, String name, String email, String role, long createdAt){
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
