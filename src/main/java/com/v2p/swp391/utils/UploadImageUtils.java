package com.v2p.swp391.utils;

import com.v2p.swp391.common.constant.Image;
import com.v2p.swp391.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UploadImageUtils {
    public static String storeFile(MultipartFile imageFile) throws IOException {
        if(imageFile.getSize() == 0) {
            throw new AppException(HttpStatus.BAD_REQUEST,"Please select images to upload");
        }
        if (!isImageFile(imageFile) || imageFile.getOriginalFilename() == null) {
            throw new AppException (HttpStatus.UNSUPPORTED_MEDIA_TYPE,"Invalid image format") ;
        }
        if (imageFile.getSize() > 3 * 1024 * 1024 ) {
            throw new AppException(HttpStatus.PAYLOAD_TOO_LARGE, "File must be <= 5MB");
        }
        BufferedImage image = ImageIO.read(imageFile.getInputStream());
        image = resize(image, 500, 550);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        String originalFileName = imageFile.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
        java.nio.file.Path uploadDir = Paths.get(Image.BIRD_IMAGE_PATH);

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        java.nio.file.Path destination = Paths.get(uploadDir.toString(),uniqueFileName);
        Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    private static boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null && contentType.startsWith("image/")) {
            return contentType.endsWith("jpeg") || contentType.endsWith("jpg") || contentType.endsWith("png");
        }
        return false;
    }
    public static BufferedImage resize(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics.dispose();
        return resizedImage;
    }

}
