package com.example.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/foods")
@CrossOrigin(origins = "*")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    // 1. Lấy danh sách đồ ăn
    @GetMapping
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    // 2. API đọc ảnh đồ ăn (dùng chung thư mục uploads)
    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("image/jpeg"))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. Thêm Đồ ăn (có tải ảnh)
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Food> addFood(
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam("imageFile") MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        Food food = new Food(name, price);
        food.setImageFileName(fileName);
        
        System.out.println("---> ADMIN ĐÃ THÊM MÓN: " + food.getName());
        return ResponseEntity.ok(foodRepository.save(food));
    }

    // 4. Cập nhật Đồ ăn
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Food> updateFood(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("price") double price,
            @RequestParam(value = "imageFile", required = false) MultipartFile file) throws IOException {
            
        Food food = foodRepository.findById(id).orElseThrow();
        food.setName(name);
        food.setPrice(price);
        
        if(file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            food.setImageFileName(fileName);
        }
        
        System.out.println("---> ADMIN ĐÃ CẬP NHẬT MÓN ID: " + id);
        return ResponseEntity.ok(foodRepository.save(food));
    }

    // 5. Xóa Đồ ăn
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable Long id) {
        foodRepository.deleteById(id);
        System.out.println("---> ADMIN ĐÃ XÓA MÓN ID: " + id);
        return ResponseEntity.ok("Đã xóa đồ ăn!");
    }
}