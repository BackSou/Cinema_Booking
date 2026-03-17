package com.example.cinema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    
    // HÀM MỚI: Trả về true/false xem tài khoản đã tồn tại chưa
    boolean existsByUsername(String username); 
}