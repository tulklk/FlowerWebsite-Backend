package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.FlowerImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/flowerImg")
@CrossOrigin("http://localhost:3000")
public class FlowerImageController {

    @Autowired
    private FlowerImageService flowerImageService;

    // API upload nhiều hình ảnh cho flower batch
    @PostMapping("/batch/{flowerID}/upload")
    public ResponseEntity<?> uploadImage(@PathVariable("flowerID") int flowerID,
                                              @RequestParam("file") MultipartFile file) {
        try {
            String uploadImg = flowerImageService.uploadImage(file, flowerID);
            return ResponseEntity.status(HttpStatus.OK).body(uploadImg);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    // API lấy hình ảnh theo batchID
    @GetMapping("/batch/{batchID}/image")
    public ResponseEntity<byte[]> downloadImageByBatchID(@PathVariable("batchID") int batchID) throws IOException {
        // Gọi phương thức trong service để lấy hình ảnh
        byte[] imageBytes = flowerImageService.downloadImageByBatchID(batchID);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Hoặc loại MIME phù hợp khác
                .body(imageBytes);
    }

    // API cập nhật ảnh cho FlowerBatch
    @PutMapping("/update/{batchID}")
    public ResponseEntity<String> updateImage(@RequestParam("image") MultipartFile file, @PathVariable("batchID") int batchID) throws IOException {
        String message = flowerImageService.updateImage(file, batchID);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // API xóa ảnh cho FlowerBatch
    @DeleteMapping("/delete/{batchID}")
    public ResponseEntity<String> deleteImagesByBatchID(@PathVariable("batchID") int batchID) {
        try {
            String message = flowerImageService.deleteImagesByBatchID(batchID);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy ảnh nào để xóa cho batchID: " + batchID);
        }
    }

    // API lấy tất cả ảnh
    @GetMapping("/all")
    public ResponseEntity<List<byte[]>> getAllImages() {
        List<byte[]> images = flowerImageService.getAllImages();
        return ResponseEntity.status(HttpStatus.OK).body(images);
    }
}
