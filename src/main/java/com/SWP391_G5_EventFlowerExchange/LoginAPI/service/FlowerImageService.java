package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.Utils.ImageUtils;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerBatch;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.FlowerImage;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IFlowerBatchRepository;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IFlowerImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlowerImageService {

    private final IFlowerImageRepository flowerImageRepository;
    private final IFlowerBatchRepository flowerBatchRepository;

    @Autowired
    public FlowerImageService(IFlowerImageRepository flowerImageRepository, IFlowerBatchRepository flowerBatchRepository) {
        this.flowerImageRepository = flowerImageRepository;
        this.flowerBatchRepository = flowerBatchRepository;
    }

    public String uploadImage(MultipartFile file, int flowerID) throws IOException {
       FlowerBatch fl=new FlowerBatch();
       if(flowerBatchRepository.findById(flowerID).isPresent()){
           fl=flowerBatchRepository.findById(flowerID).get();
       }else{
           throw new FileNotFoundException("File not found");
       }
       FlowerImage flowerImage=flowerImageRepository.save(
               FlowerImage.builder()
                       .name(file.getContentType())
                       .imageData(ImageUtils.compressImage(file.getBytes()))
                       .flowerBatch(fl)
                       .build()

       );
       if(flowerImage!=null){
           return "file anh upload thanh cong:"+file.getOriginalFilename();
       }
       return null;
    }

    // Phương thức download hình ảnh
    public byte[] downloadImageByBatchID(int batchID) throws IOException {
        // Tìm FlowerBatch theo ID
        FlowerBatch flowerBatch = flowerBatchRepository.findById(batchID)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy FlowerBatch với ID " + batchID));

        // Lấy hình ảnh đầu tiên trong danh sách hình ảnh liên quan đến FlowerBatch
        Optional<FlowerImage> imageOptional = flowerImageRepository.findByFlowerBatch(flowerBatch).stream().findFirst();

        // Nếu không tìm thấy hình ảnh nào, ném ngoại lệ
        return imageOptional.map(img -> ImageUtils.decompressImage(img.getImageData()))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hình ảnh cho batch ID: " + batchID));
    }
    // Hàm cập nhật ảnh
    public String updateImage(MultipartFile file, int batchID) throws IOException {
        FlowerBatch flowerBatch = flowerBatchRepository.findById(batchID)
                .orElseThrow(() -> new FileNotFoundException("Batch with ID " + batchID + " not found"));

        Optional<FlowerImage> existingImage = flowerImageRepository.findByFlowerBatch(flowerBatch).stream().findFirst();

        if (existingImage.isPresent()) {
            FlowerImage flowerImage = existingImage.get();
            flowerImage.setName(file.getOriginalFilename());
            flowerImage.setType(file.getContentType());
            flowerImage.setImageData(ImageUtils.compressImage(file.getBytes()));

            flowerImageRepository.save(flowerImage);

            return "Image updated successfully for batchID: " + batchID;
        } else {
            throw new RuntimeException("Image not found for batchID: " + batchID);
        }
    }

    // Hàm xóa ảnh
    public String deleteImagesByBatchID(int batchID) {
        FlowerBatch flowerBatch = flowerBatchRepository.findById(batchID)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh với ID " + batchID));

        List<FlowerImage> flowerImages = flowerImageRepository.findByFlowerBatch(flowerBatch);
        if (flowerImages.isEmpty()) {
            throw new RuntimeException("Không tìm thấy ảnh nào để xóa cho batchID: " + batchID);
        }

        flowerImageRepository.deleteAll(flowerImages);
        return "Đã xóa tất cả ảnh thành công cho batchID: " + batchID;
    }

    // Hàm lấy tất cả ảnh
    public List<byte[]> getAllImages() {
        return flowerImageRepository.findAll().stream()
                .map(image -> ImageUtils.decompressImage(image.getImageData()))
                .collect(Collectors.toList());
    }
}
