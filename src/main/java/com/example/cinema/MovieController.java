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

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public MovieController() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo thư mục lưu file!", ex);
        }
    }

    //API LẤY DANH SÁCH PHIM
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // API GIÚP TRÌNH DUYỆT ĐỌC ĐƯỢC ẢNH
    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
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

    // API POST
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Movie> addMovieWithImage(
            @RequestParam("title") String title,
            @RequestParam("price") double price,
            @RequestParam("posterFile") MultipartFile file) throws IOException {

        String fileName = saveFile(file);

        Movie movie = new Movie(title, price);
        movie.setPosterFileName(fileName); 
        
        System.out.println("---> ADMIN ĐÃ THÊM PHIM & ẢNH: " + movie.getTitle());
        return ResponseEntity.ok(movieRepository.save(movie));
    }

    // PUT
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
        
        if(file != null && !file.isEmpty()) {
            String newFileName = saveFile(file);
            movie.setPosterFileName(newFileName);
        }
        
        System.out.println("---> ADMIN ĐÃ CẬP NHẬT PHIM ID: " + id);
        return ResponseEntity.ok(movieRepository.save(movie));
    }

    // API XÓA 
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        movieRepository.deleteById(id);
        System.out.println("---> ADMIN ĐÃ XÓA PHIM ID: " + id);
        return ResponseEntity.ok("Đã xóa phim thành công!");
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }

    
}