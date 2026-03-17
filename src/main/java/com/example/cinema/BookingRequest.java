package com.example.cinema;
import java.util.List;

public class BookingRequest {
    private String username;
    private String movieName;
    private String showDate;  // Mới
    private String showTime;  // Mới
    private String room;
    private List<String> seats;
    private String foods;
    private int totalPrice;

    // Getters và Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }
    public String getShowDate() { return showDate; }
    public void setShowDate(String showDate) { this.showDate = showDate; }
    public String getShowTime() { return showTime; }
    public void setShowTime(String showTime) { this.showTime = showTime; }
    public List<String> getSeats() { return seats; }
    public void setSeats(List<String> seats) { this.seats = seats; }
    public String getFoods() { return foods; }
    public void setFoods(String foods) { this.foods = foods; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}