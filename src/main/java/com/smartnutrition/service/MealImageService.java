package com.smartnutrition.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class MealImageService {

    @Value("${meal.image.upload-dir:uploads}")
    private String uploadDir;

    /**
     * Stores an uploaded meal image and returns its relative web path.
     * Generates a unique filename using UUID to prevent collisions.
     */
    public String storeImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store empty file.");
        }

        // Get extension
        String originalFilename = file.getOriginalFilename();
        String extension = "jpg"; // default fallback
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = StringUtils.getFilenameExtension(originalFilename);
        }

        // Validate extension (simple image checks)
        if (extension == null || !isImageExtension(extension)) {
            throw new IllegalArgumentException("Only image files (jpg, jpeg, png, webp) are allowed.");
        }

        // Ensure directories exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String filename = UUID.randomUUID().toString() + "." + extension;
        Path targetLocation = uploadPath.resolve(filename);

        // Copy file
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        }

        // Return path mapped to the resource handler (starts with /uploads/)
        return "/uploads/" + filename;
    }

    private boolean isImageExtension(String extension) {
        String ext = extension.toLowerCase();
        return ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || ext.equals("webp");
    }
}
