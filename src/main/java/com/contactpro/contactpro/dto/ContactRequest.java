package com.contactpro.contactpro.dto;

/*
 * This DTO represents data sent from frontend
 */
public class ContactRequest {

    private String name;
    private String phone;
    private String email;
    private String category;
    private String notes;

    private Long userId;  // required to assign contact to user

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getCategory() { return category; }
    public String getNotes() { return notes; }
    public Long getUserId() { return userId; }
}