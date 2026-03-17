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

    // 1. API ĐẶT VÉ (Lưu thành 1 hóa đơn)
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

    // 2. API LẤY GHẾ ĐÃ BÁN (Cắt chuỗi ra lại thành mảng)
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

    // 3. API MỚI: TẢI LỊCH SỬ MUA HÀNG (Mỗi lần 20 đơn)
    @GetMapping("/history")
    public ResponseEntity<List<Booking>> getHistory(
            @RequestParam String username, 
            @RequestParam(defaultValue = "0") int page) {
        
        // Tạo yêu cầu phân trang: trang số 'page', mỗi trang 20 phần tử
        Pageable pageable = PageRequest.of(page, 20); 
        List<Booking> history = bookingRepository.findByUsernameOrderByIdDesc(username, pageable);
        
        return ResponseEntity.ok(history);
    }

    // 4. API XÓA GHẾ THEO PHIM (Dành cho Admin)
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