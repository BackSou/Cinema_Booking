package com.example.cinema;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String movieName;
    private String seatNumber;

    // Constructors
    public Booking() {}
    
    public Booking(String movieName, String seatNumber) {
        this.movieName = movieName;
        this.seatNumber = seatNumber;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
}