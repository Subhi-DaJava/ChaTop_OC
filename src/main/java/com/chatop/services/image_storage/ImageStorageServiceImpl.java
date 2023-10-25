package com.chatop.services.image_storage;

import com.chatop.exceptions.InvalidImageFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@Transactional
public class ImageStorageServiceImpl implements ImageStorageService {

    @Value("${image-storage-path}")
    private String imageStoragePath;

    @Override
    public String savePicture(MultipartFile image) throws IOException {
        String contentType = image.getContentType();

        if (contentType == null ) {
            log.error("The content type of the image is unknown.");
            throw new InvalidImageFormatException("The content type of the image is unknown.");
        }
        if (!(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/jpg"))) {
            log.error("Only images in JPG, PNG or JPEG format are accepted.");
            throw new InvalidImageFormatException("Only images in JPG, PNG or JPEG format are accepted.");
        }
        try {
            Path path = Paths.get(imageStoragePath, image.getOriginalFilename());
            Files.write(path, image.getBytes());
            log.info("Rental Image saved successfully");
            return image.getOriginalFilename();
        } catch (IOException e) {
            log.error("Error while saving rental image");
            throw e;
        }
    }
}
