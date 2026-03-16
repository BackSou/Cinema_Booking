package com.example.cinema;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password; // Thực tế sẽ phải mã hóa, nhưng dự án nhỏ ta để chuỗi thường cho dễ
    private String role; // Sẽ lưu chữ "ADMIN" hoặc "USER"

    // Constructors
    public Account() {}

    public Account(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}