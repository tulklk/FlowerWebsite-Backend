package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.Utils.ImageUtils;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.EventFlowerPosting;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Image;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IEventFlowerPostingRepository;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {
    private ImageRepository imageRepository;
    private IEventFlowerPostingRepository eventFlowerPostingRepository;
    @Autowired
    public ImageService(ImageRepository imageRepository, IEventFlowerPostingRepository eventFlowerPostingRepository) {
        this.imageRepository = imageRepository;
        this.eventFlowerPostingRepository = eventFlowerPostingRepository;
    }
    public String uploadImage(MultipartFile file,int postID) throws IOException {
        EventFlowerPosting eventFlowerPosting = new EventFlowerPosting();
        if(eventFlowerPostingRepository.findById(postID).isPresent()){
            eventFlowerPosting=eventFlowerPostingRepository.findById(postID).get();
        }else {
            throw new FileNotFoundException("File not found");
        }
        Image imageData = imageRepository.save(
                Image.builder()
                .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .imageData(ImageUtils.compressImage(file.getBytes()))
                        .eventFlowerPosting(eventFlowerPosting)
                        .build()
                );
        if (imageData!=null){
            return "file anh upload thanh cong:" +file.getOriginalFilename();
        }return null;
    }
    public byte[] downloadImageByPostID(int postID) throws IOException {
        // Tìm bài đăng theo ID
        EventFlowerPosting posting = eventFlowerPostingRepository.findById(postID)
                .orElseThrow(() -> new RuntimeException("Post with ID " + postID + " not found"));

        // Lấy hình ảnh liên quan đến bài đăng
        Optional<Image> image = imageRepository.findByEventFlowerPosting(posting).stream().findFirst();

        // Nếu không có hình ảnh nào được tìm thấy, ném ngoại lệ
        return image.map(img -> ImageUtils.decompressImage(img.getImageData()))
                .orElseThrow(() -> new RuntimeException("No images found for post ID: " + postID));
    }
    // Hàm cập nhật ảnh
    public String updateImage(MultipartFile file, int postID) throws IOException {
        // Tìm bài đăng theo ID
        EventFlowerPosting eventFlowerPosting = eventFlowerPostingRepository.findById(postID)
                .orElseThrow(() -> new FileNotFoundException("Post with ID " + postID + " not found"));

        // Tìm hình ảnh hiện tại liên quan đến bài đăng
        Optional<Image> existingImage = imageRepository.findByEventFlowerPosting(eventFlowerPosting).stream().findFirst();

        // Nếu tìm thấy ảnh, cập nhật dữ liệu ảnh
        if (existingImage.isPresent()) {
            Image image = existingImage.get();
            image.setName(file.getOriginalFilename());
            image.setType(file.getContentType());
            image.setImageData(ImageUtils.compressImage(file.getBytes()));  // Nén và cập nhật dữ liệu ảnh mới

            // Lưu lại ảnh đã cập nhật vào cơ sở dữ liệu
            imageRepository.save(image);

            return "Image updated successfully for postID: " + postID;
        } else {
            // Nếu không tìm thấy ảnh, ném ngoại lệ hoặc xử lý phù hợp
            throw new RuntimeException("Image not found for postID: " + postID);
        }
    }
    public String deleteImage(int postID) {
        // Tìm bài đăng theo ID
        EventFlowerPosting eventFlowerPosting = eventFlowerPostingRepository.findById(postID)
                .orElseThrow(() -> new RuntimeException("Post with ID " + postID + " not found"));

        // Tìm ảnh liên quan đến bài đăng
        Optional<Image> image = imageRepository.findByEventFlowerPosting(eventFlowerPosting).stream().findFirst();

        if (image.isPresent()) {
            // Chuyển đổi id từ int sang Long để xóa
            imageRepository.deleteById(Long.valueOf(image.get().getId()));
            return "Image deleted successfully for postID: " + postID;
        } else {
            throw new RuntimeException("Image not found for postID: " + postID);
        }
    }
    public List<byte[]> getAllImages() {
        // Lấy tất cả các ảnh từ cơ sở dữ liệu và giải nén dữ liệu ảnh
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(image -> ImageUtils.decompressImage(image.getImageData()))
                .collect(Collectors.toList());
    }

}
