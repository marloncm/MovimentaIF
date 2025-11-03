package com.ifrs.movimentaif_app.model;

public class User {
    private String role;
    private String userId;
    private String userName;

    public User() {}

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}