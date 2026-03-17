package com.example.cinema;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByMovieNameAndRoomAndShowDateAndShowTime(String movieName, String room, String showDate, String showTime);
    @Transactional
void deleteByMovieNameAndRoomAndShowDateAndShowTime(String movieName, String room, String showDate, String showTime);
    List<Booking> findByUsernameOrderByIdDesc(String username, Pageable pageable);
}