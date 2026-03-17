package com.example.cinema;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // NÂNG CẤP: Tìm hóa đơn theo đúng Suất chiếu (Phim + Ngày + Giờ)
    List<Booking> findByMovieNameAndRoomAndShowDateAndShowTime(String movieName, String room, String showDate, String showTime);
    
    // NÂNG CẤP: Xóa ghế theo đúng Suất chiếu
    @Transactional
void deleteByMovieNameAndRoomAndShowDateAndShowTime(String movieName, String room, String showDate, String showTime);

    List<Booking> findByUsernameOrderByIdDesc(String username, Pageable pageable);
}