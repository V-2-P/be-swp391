package com.v2p.swp391.utils;

import com.v2p.swp391.common.constant.Image;
import com.v2p.swp391.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UploadImageUtils {
    public static String storeFile(MultipartFile imageFile) throws IOException {
        if (imageFile.getSize() > 10 * 1024 * 1024) {
            throw new AppException(HttpStatus.BAD_REQUEST, "File size exceeds 10MB");
        }
        if(imageFile.getSize() == 0) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Please upload file image ");
        }
        if (!isImageFile(imageFile) || imageFile.getOriginalFilename() == null) {
            throw new AppException (HttpStatus.UNSUPPORTED_MEDIA_TYPE,"Invalid image format") ;
        }
        String originalFileName = imageFile.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
        java.nio.file.Path uploadDir = Paths.get(Image.BIRD_IMAGE_PATH);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        java.nio.file.Path destination = Paths.get(uploadDir.toString(),uniqueFileName);
        Files.copy(imageFile.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    private static boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

}
