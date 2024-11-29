package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/img")
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageController {
    ImageService imageService;

    @PostMapping("/{postID}")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file, @PathVariable("postID") int postID)
    throws IOException {
        String uploadImage =imageService.uploadImage(file,postID);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }

    @GetMapping("/{postID}")
    public ResponseEntity<?> downloadImageByPostID(@PathVariable("postID") int postID) throws IOException {
        byte[] imageData = imageService.downloadImageByPostID(postID);
        return ResponseEntity.status(HttpStatus.OK).contentType(  MediaType.valueOf("image/jpeg"))
                .body(imageData);
    }

    // Update image
    @PutMapping("/{postID}")
    public ResponseEntity<?> updateImage(@RequestParam("image") MultipartFile file, @PathVariable("postID") int postID)
            throws IOException {
        String updateMessage = imageService.updateImage(file, postID);
        return ResponseEntity.status(HttpStatus.OK).body(updateMessage);
    }

    // Delete image
    @DeleteMapping("/{postID}")
    public ResponseEntity<?> deleteImage(@PathVariable("postID") int postID) {
        String deleteMessage = imageService.deleteImage(postID);
        return ResponseEntity.status(HttpStatus.OK).body(deleteMessage);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllImages() {
        List<byte[]> images = imageService.getAllImages();
        return ResponseEntity.status(HttpStatus.OK).body(images);
    }
}
