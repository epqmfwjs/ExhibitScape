package com.ExhibitScape.app.controller.member;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ExhibitScape.app.dto.member.MemberDetails;
import com.ExhibitScape.app.service.member.MyPagePWChangeService;

@Controller
@RequestMapping("/myPage/changePassword")
public class MyPagePWChangeController {

    private final MyPagePWChangeService myPagePWChangeService;

    public MyPagePWChangeController(MyPagePWChangeService myPagePWChangeService) {
        this.myPagePWChangeService = myPagePWChangeService;
    }

    @GetMapping
    public String changePasswordForm(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        model.addAttribute("memberId", memberDetails.getUsername());
        return "member/MyPagePWChange";
    }

    @PostMapping
    public String changePassword(@AuthenticationPrincipal MemberDetails memberDetails,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {
        String memberId = memberDetails.getUsername();

        if (myPagePWChangeService.isCurrentPasswordCorrect(memberId, currentPassword)) {
            if (newPassword.equals(confirmPassword)) {
                myPagePWChangeService.changePassword(memberId, newPassword);
                model.addAttribute("success", true);
            } else {
                model.addAttribute("error", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            }
        } else {
            model.addAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
        }

        model.addAttribute("memberId", memberId);
        return "member/MyPagePWChange";
    }
}