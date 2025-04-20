package com.example.flattie.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.flattie.repository.AppUserRepository;
import com.example.flattie.model.AppUser;
import com.example.flattie.service.ImageService;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private AppUserRepository appUserRepository;

    @PostMapping("/api/upload/profile")
    public String uploadProfileImage(@RequestParam MultipartFile image,
            @AuthenticationPrincipal AppUser user) {
        try {
            String path = imageService.saveImage(image, "profiles");
            user.setProfileImage(path);
            appUserRepository.save(user);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error";
        }
        return "redirect:/profile";
    }

}
