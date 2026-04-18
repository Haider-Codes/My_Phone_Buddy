package com.myphonebuddy.utility.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        if(file == null || file.isEmpty()){
            return true; // Since contact image is not mandatory, but if user is adding it above validation should occur.
        }
        // Handling multipart file validation
        String fileContentType = file.getContentType();

            boolean isValidContentType = Objects.equals(fileContentType, "image/jpeg") || Objects.equals(fileContentType, "image/png");
            boolean isValidSize = file.getSize() <= 2*1024*1024;

            return isValidContentType && isValidSize;

    }
}
