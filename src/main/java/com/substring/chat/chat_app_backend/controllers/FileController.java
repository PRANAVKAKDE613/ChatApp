package com.substring.chat.chat_app_backend.controllers;

import com.substring.chat.chat_app_backend.config.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = AppConstants.FRONT_END_BASE_URL)
public class FileController {

    private final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        try {

            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());

            Files.write(path, file.getBytes());

            return ResponseEntity.ok(
                    "http://localhost:8080/uploads/" + file.getOriginalFilename()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}