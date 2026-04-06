package com.contactpro.contactpro.dto;

import java.time.LocalDateTime;

public class ContactResponse {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String category;
    private boolean isBlocked;
    private boolean isFavorite;
    private LocalDateTime createdAt;

    public ContactResponse(Long id, String name, String phone,
                           String email, String category,
                           boolean isBlocked, boolean isFavorite,
                           LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.category = category;
        this.isBlocked = isBlocked;
        this.isFavorite = isFavorite;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getCategory() { return category; }
    public boolean isBlocked() { return isBlocked; }
    public boolean isFavorite() { return isFavorite; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}