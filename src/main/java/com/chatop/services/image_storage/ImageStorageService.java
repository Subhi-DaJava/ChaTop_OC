package com.chatop.services.image_storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {

    String savePicture(MultipartFile image) throws IOException;
}
