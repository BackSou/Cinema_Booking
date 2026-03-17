package com.example.cinema;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private double price;
    
    // Đổi tên để lưu TÊN FILE thay vì URL
    private String posterFileName; 

    public Movie() {}

    public Movie(String title, double price) {
        this.title = title;
        this.price = price;
    }

    // Getters và Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getPosterFileName() { return posterFileName; }
    public void setPosterFileName(String posterFileName) { this.posterFileName = posterFileName; }
}