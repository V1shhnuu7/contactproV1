package com.contactpro.contactpro.dto;

public class LoginResponse {

    private String message;
    private Long userId;
    private String email;

    public LoginResponse(String message, Long userId, String email) {
        this.message = message;
        this.userId = userId;
        this.email = email;
    }

    public String getMessage() { return message; }
    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
}