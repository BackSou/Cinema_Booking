package com.example.cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Thư viện để nhận file
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
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    // Đường dẫn thư mục để lưu ảnh trên máy tính của bạn
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public MovieController() {
        // Tự động tạo thư mục 'uploads' nếu nó chưa tồn tại
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo thư mục lưu file!", ex);
        }
    }

    // 1. API LẤY DANH SÁCH PHIM
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // 2. API PHỤ: GIÚP TRÌNH DUYỆT ĐỌC ĐƯỢC ẢNH
    // Khi web khách gọi tới: http://localhost:8080/api/movies/image/ten_file.jpg
    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                // Tự động xác định loại ảnh (png, jpg, webp...)
                String contentType = "image/jpeg";
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. API THÊM PHIM MỚI + TẢI ẢNH (POST)
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Movie> addMovieWithImage(
            @RequestParam("title") String title,
            @RequestParam("price") double price,
            @RequestParam("posterFile") MultipartFile file) throws IOException {

        // Bước A: Lưu file ảnh
        String fileName = saveFile(file);

        // Bước B: Tạo đối tượng phim và lưu vào DB
        Movie movie = new Movie(title, price);
        movie.setPosterFileName(fileName); // Lưu tên file vào database
        
        System.out.println("---> ADMIN ĐÃ THÊM PHIM & ẢNH: " + movie.getTitle());
        return ResponseEntity.ok(movieRepository.save(movie));
    }

    // 4. API CẬP NHẬT PHIM (PUT)
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Movie> updateMovie(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("price") double price,
            @RequestParam(value = "posterFile", required = false) MultipartFile file // Ảnh là không bắt buộc khi sửa
    ) throws IOException {
        Movie movie = movieRepository.findById(id).orElseThrow();
        
        movie.setTitle(title);
        movie.setPrice(price);
        
        // Nếu admin có tải lên ảnh mới
        if(file != null && !file.isEmpty()) {
            // Lưu ảnh mới
            String newFileName = saveFile(file);
            movie.setPosterFileName(newFileName);
        }
        
        System.out.println("---> ADMIN ĐÃ CẬP NHẬT PHIM ID: " + id);
        return ResponseEntity.ok(movieRepository.save(movie));
    }

    // 5. API XÓA PHIM
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieRepository.deleteById(id);
        System.out.println("---> ADMIN ĐÃ XÓA PHIM ID: " + id);
        return ResponseEntity.ok("Đã xóa phim thành công!");
    }

    // --- HÀM PHỤ ĐỂ LƯU FILE ---
    private String saveFile(MultipartFile file) throws IOException {
        // Tạo một tên file duy nhất (để không bị trùng)
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        // Đường dẫn đầy đủ để lưu file
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        
        // Copy file ảnh vào thư mục 'uploads'
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }
}