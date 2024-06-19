package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private AmazonS3Service amazonS3Service;

    @PostMapping("/upload-to-cloud-dinary")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<String> urls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String url = cloudinaryService.uploadFile(file);
                urls.add(url);
            }
            return new ResponseEntity<>(urls, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-to-amazon")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = amazonS3Service.uploadFile(file);
            return new ResponseEntity<>(fileName, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download-from-amazon")
    public ResponseEntity<String> getFileUrl(@RequestParam("fileName") String fileName) {
        String url = amazonS3Service.getFileUrl(fileName);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }
}