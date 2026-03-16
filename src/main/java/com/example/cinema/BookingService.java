package com.example.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Xử lý nghiệp vụ đặt vé.
     * Sử dụng @Transactional để đảm bảo tất cả vé được lưu hoặc không vé nào được lưu.
     */
    @Transactional
    public void bookTickets(BookingRequest request) {
        String movie = request.getMovieName();
        List<String> seats = request.getSeats();

        // TODO: Thêm logic kiểm tra xem ghế đã được đặt trước đó chưa để tránh lỗi concurrency.
        // Ví dụ: `if (bookingRepository.existsByMovieNameAndSeatNumberIn(movie, seats)) { ... }`

        List<Booking> newBookings = seats.stream()
                .map(seat -> new Booking(movie, seat))
                .collect(Collectors.toList());

        // Sử dụng saveAll để lưu nhiều bản ghi trong một lần gọi, hiệu quả hơn.
        bookingRepository.saveAll(newBookings);
    }

    public List<String> getBookedSeats(String movieName) {
        List<Booking> bookings = bookingRepository.findByMovieName(movieName);
        return bookings.stream()
                .map(Booking::getSeatNumber)
                .collect(Collectors.toList());
    }

    public void clearAllBookings() {
        bookingRepository.deleteAll();
    }
}