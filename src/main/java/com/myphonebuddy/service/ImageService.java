package com.myphonebuddy.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile file, String imageFileName);

    String generateUploadImageUrl(String fileName);

}
