package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.Notice;
import com.example.flattie.service.NoticeService;

@Controller
public class NoticeBoardController {

    @Autowired
    NoticeService noticeService;

    @PostMapping("/createNotice")
    public String createNotice(@RequestParam("title") String title,
            @RequestParam("description") String description,
            @AuthenticationPrincipal AppUser user) {
        Flat userFlat = user.getFlat();
        Notice newNotice = new Notice(title, description, user, userFlat);

        userFlat.addNotice(newNotice);
        noticeService.saveNotice(newNotice);

        return "redirect:/";
    }
}
