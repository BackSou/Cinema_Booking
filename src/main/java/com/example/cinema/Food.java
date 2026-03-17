package com.example.cinema;

import jakarta.persistence.*;

@Entity
@Table(name = "foods")
public class Food {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;        // Tên combo (VD: Combo Bắp Phô Mai + 2 Nước)
    private double price;       // Giá tiền
    private String imageFileName; // Tên file ảnh

    public Food() {}

    public Food(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Getters và Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImageFileName() { return imageFileName; }
    public void setImageFileName(String imageFileName) { this.imageFileName = imageFileName; }
}