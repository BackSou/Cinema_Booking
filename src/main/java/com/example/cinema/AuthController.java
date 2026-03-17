package com.example.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account loginRequest) {
        Account account = accountRepository.findByUsername(loginRequest.getUsername());
        if (account != null && account.getPassword().equals(loginRequest.getPassword())) {
            Map<String, String> response = new HashMap<>();
            response.put("username", account.getUsername());
            response.put("role", account.getRole() != null ? account.getRole() : "USER");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tên đăng nhập hoặc mật khẩu!");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Account newAccount) {
        if (accountRepository.existsByUsername(newAccount.getUsername())) {
            return ResponseEntity.badRequest().body("Tài khoản đã tồn tại!");
        }
        // Ép cứng role là USER
        newAccount.setRole("USER");
        accountRepository.save(newAccount);
        System.out.println("---> ĐĂNG KÝ MỚI: " + newAccount.getUsername() + " | SĐT: " + newAccount.getPhone());
        return ResponseEntity.ok("Đăng ký thành công!");
    }

    // API MỚI: Check username trực tiếp lúc đang gõ
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = accountRepository.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
}