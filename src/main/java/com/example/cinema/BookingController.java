package com.example.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    // 1. API ĐẶT VÉ MỚI
    @PostMapping("/book")
    public ResponseEntity<String> bookTickets(@RequestBody BookingRequest request) {
        String movie = request.getMovieName();
        List<String> seats = request.getSeats();
        
        // Lưu từng ghế vào Database
        for (String seat : seats) {
            Booking newBooking = new Booking(movie, seat);
            bookingRepository.save(newBooking);
        }
        
        // In ra terminal để theo dõi
        System.out.println("---> CÓ KHÁCH ĐẶT VÉ!");
        System.out.println("Phim: " + movie + " | Ghế: " + seats);
        
        return ResponseEntity.ok("Thành công! Bạn đã đặt " + seats.size() + " ghế cho phim " + movie + ".");
    }

    // 2. API TẢI GHẾ ĐÃ BÁN CỦA 1 PHIM
    @GetMapping("/booked-seats")
    public ResponseEntity<List<String>> getBookedSeats(@RequestParam String movie) {
        // In ra terminal để theo dõi
        System.out.println("---> YÊU CẦU KIỂM TRA GHẾ");
        System.out.println("Tên phim khách vừa chọn: " + movie);
        
        // Tìm trong Database các vé của đúng phim này
        List<Booking> bookings = bookingRepository.findByMovieName(movie);
        
        // Trích xuất chỉ lấy danh sách mã ghế (ví dụ: ["A1", "A2"])
        List<String> bookedSeats = bookings.stream()
                .map(Booking::getSeatNumber)
                .collect(Collectors.toList());
                
        System.out.println("Các ghế đã có người ngồi: " + bookedSeats);
        return ResponseEntity.ok(bookedSeats);
    }

    @Autowired
    private AccountRepository accountRepository;

    // API ĐĂNG NHẬP (CẢNH BÁO BẢO MẬT)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        // 1. Tìm tài khoản trong Database
        Account account = accountRepository.findByUsername(request.getUsername());

        // 2. Kiểm tra xem tài khoản có tồn tại và sai mật khẩu không
        if (account == null || !account.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Sai tài khoản hoặc mật khẩu!");
        }

        // 3. Nếu đúng, trả về quyền (Role) của người đó
        return ResponseEntity.ok(account.getRole());
    }

    // 3. API XÓA SẠCH DỮ LIỆU ĐẶT VÉ (Cần được bảo vệ, chỉ cho phép Admin)
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllBookings() {
        // Hàm deleteAll() có sẵn của Spring Data JPA
        bookingRepository.deleteAll();
        
        System.out.println("---> ĐÃ XÓA TOÀN BỘ DỮ LIỆU GHẾ!");
        return ResponseEntity.ok("Đã reset toàn bộ rạp phim thành công.");
    }
}