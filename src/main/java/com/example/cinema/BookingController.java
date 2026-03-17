package com.example.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    // API ĐẶT VÉ 
    @PostMapping("/book")
    public ResponseEntity<String> bookTickets(@RequestBody BookingRequest request) {
        Booking booking = new Booking();
        booking.setUsername(request.getUsername());
        booking.setMovieName(request.getMovieName());
        booking.setRoom(request.getRoom()); // Lưu Rạp
        booking.setShowDate(request.getShowDate()); 
        booking.setShowTime(request.getShowTime()); 
        booking.setSeats(String.join(", ", request.getSeats()));
        booking.setFoods(request.getFoods());
        booking.setTotalPrice(request.getTotalPrice());
        
        bookingRepository.save(booking);
        return ResponseEntity.ok("Thành công");
    }

    // API LẤY GHẾ ĐÃ BÁN (
   @GetMapping("/booked-seats")
    public ResponseEntity<List<String>> getBookedSeats(
            @RequestParam String movie, 
            @RequestParam String room, 
            @RequestParam String date, 
            @RequestParam String time) {
        
        List<Booking> bookings = bookingRepository.findByMovieNameAndRoomAndShowDateAndShowTime(movie, room, date, time);
        List<String> allBookedSeats = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getSeats() != null && !b.getSeats().isEmpty()) {
                allBookedSeats.addAll(Arrays.asList(b.getSeats().split(", ")));
            }
        }
        return ResponseEntity.ok(allBookedSeats);
    }

    // API TẢI LỊCH SỬ MUA HÀNG 
    @GetMapping("/history")
    public ResponseEntity<List<Booking>> getHistory(
            @RequestParam String username, 
            @RequestParam(defaultValue = "0") int page) {
        
        // phân trang
        Pageable pageable = PageRequest.of(page, 20); 
        List<Booking> history = bookingRepository.findByUsernameOrderByIdDesc(username, pageable);
        
        return ResponseEntity.ok(history);
    }

    // API XÓA GHẾ THEO PHIM (admin)
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearBookingsByMovie(
            @RequestParam String movie, 
            @RequestParam String room, 
            @RequestParam String date, 
            @RequestParam String time) {
            
        bookingRepository.deleteByMovieNameAndRoomAndShowDateAndShowTime(movie, room, date, time);
        return ResponseEntity.ok("Đã reset ghế.");
    }
}