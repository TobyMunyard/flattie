package com.example.flattie.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    public String saveImage(MultipartFile file, String subfolder) throws IOException {
        if (file == null || file.isEmpty()) {
            System.out.println("[ImageService] Image was null or empty — skipping save.");
            return null;
        }

        String uploadsBase = new File(System.getProperty("user.dir"), "uploads/" + subfolder).getAbsolutePath();
        File dir = new File(uploadsBase);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("[ImageService] Created directory: " + dir.getAbsolutePath() + " → " + created);
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destination = new File(dir, filename);
        System.out.println("[ImageService] Saving to: " + destination.getAbsolutePath());

        try {
            file.transferTo(destination);
        } catch (IOException e) {
            System.out.println("[ImageService] ERROR SAVING IMAGE: " + e.getMessage());
            throw e; // let controller catch and show error
        }

        return "/uploads/" + subfolder + "/" + filename;
    }

}