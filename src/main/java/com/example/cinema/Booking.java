package com.example.cinema;
import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;    
    private String movieName;   
    
    // 2 CỘT MỚI THÊM
    private String showDate;    // Ngày chiếu (VD: 20/10/2026)
    private String showTime;    // Giờ chiếu (VD: 18:00)
    private String room;

    private String seats;       
    private String foods;       
    private int totalPrice;     

    public Booking() {}

    // Getters và Setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }
    public String getShowDate() { return showDate; }
    public void setShowDate(String showDate) { this.showDate = showDate; }
    public String getShowTime() { return showTime; }
    public void setShowTime(String showTime) { this.showTime = showTime; }
    public String getSeats() { return seats; }
    public void setSeats(String seats) { this.seats = seats; }
    public String getFoods() { return foods; }
    public void setFoods(String foods) { this.foods = foods; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}