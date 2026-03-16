package com.example.cinema;

import java.util.List;

public class BookingRequest {
    private String movieName;
    private List<String> seats;

    // Bắt buộc phải có constructor rỗng
    public BookingRequest() {}

    // Getters và Setters
    public String getMovieName() { 
        return movieName; 
    }
    
    public void setMovieName(String movieName) { 
        this.movieName = movieName; 
    }

    public List<String> getSeats() { 
        return seats; 
    }
    
    public void setSeats(List<String> seats) { 
        this.seats = seats; 
    }
}