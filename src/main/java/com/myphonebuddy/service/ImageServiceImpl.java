package com.myphonebuddy.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.myphonebuddy.constants.AppConstants;
import com.myphonebuddy.exception.ImageUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile image, String imageFileName) {

        // Handling image
        try {
            byte[] image_data_stream = new byte[image.getInputStream().available()];
            // Transforming image into data stream
            image.getInputStream().read(image_data_stream);
            // Upload on cloudinary
            cloudinary.uploader().upload(image_data_stream, ObjectUtils.asMap(
                    "public_id", imageFileName,
                            "overwrite", true,
                            "invalidate", true));
        } catch (IOException e) {
         //   e.printStackTrace();
            throw new ImageUploadException("Some exception occurred while uploading image to Cloudinary");
        }

        return generateUploadImageUrl(imageFileName);
    }

    @Override
    public String generateUploadImageUrl(String fileName) {
        // Transforming image with specific width, height and generating an image url
        return cloudinary.url()
                .transformation(new Transformation<>()
                        .width(AppConstants.IMAGE_WIDTH)
                        .height(AppConstants.IMAGE_HEIGHT)
                        .crop(AppConstants.IMAGE_CROP_FILL))
                .generate(fileName);
    }
}
