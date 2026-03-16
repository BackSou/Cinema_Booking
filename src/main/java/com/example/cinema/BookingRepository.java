package com.example.cinema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Câu lệnh "thần thánh" giúp phân tách ghế theo phim
    List<Booking> findByMovieName(String movieName);
}