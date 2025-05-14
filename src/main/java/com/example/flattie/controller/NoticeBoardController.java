package com.example.flattie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.flattie.model.AppUser;
import com.example.flattie.model.Flat;
import com.example.flattie.model.Notice;
import com.example.flattie.repository.FlatRepository;
import com.example.flattie.service.FlatService;
import com.example.flattie.service.NoticeService;

/**
 * Controller for adding and removing notices from a flat noticeboard.
 */
@Controller
public class NoticeBoardController {

    @Autowired
    NoticeService noticeService;

    @Autowired
    FlatService flatService;

    @Autowired
    FlatRepository flatRepository;

    /**
     * Creates a new notice to display on the flat noticeboard.
     * 
     * @param title       The title of the notice.
     * @param description A description of the notice specifics.
     * @param user        The user who created the notice (currently logged in).
     * @return A redirect to the home page (where the noticeboard is).
     */
    @PostMapping("/createNotice")
    public String createNotice(@RequestParam("title") String title,
            @RequestParam("description") String description,
            @AuthenticationPrincipal AppUser user) {
        Flat userFlat = flatRepository.findById(user.getFlat().getId())
                .orElseThrow(() -> new RuntimeException("Flat not found"));
        Notice newNotice = new Notice(title, description, user, userFlat);

        userFlat.addNotice(newNotice);
        noticeService.saveNotice(newNotice);

        return "redirect:/";
    }

    /**
     * Deletes a notice from the noticeboard. Called when "X" button is clicked.
     * 
     * @param noticeId The id number of the notice being deleted.
     * @param user     The user who is currently logged in.
     * @return A redirect to the home page (where the noticeboard is).
     */
    @PostMapping("/deleteNotice")
    public String deleteNotice(@RequestParam("noticeId") Long noticeId, @AuthenticationPrincipal AppUser user) {
        Flat userFlat = flatRepository.findByIdWithNotices(user.getFlat().getId()).orElse(null);
        if (userFlat == null) {
            // Redirect to home, couldn't get user flat for some reason. Should never happen
            return "redirect:/";
        }
        Notice noticeToDelete = noticeService.getNoticeById(noticeId);

        userFlat.removeNotice(noticeToDelete);
        flatService.saveFlat(userFlat);

        noticeService.deleteById(noticeId);
        return "redirect:/";
    }
}
