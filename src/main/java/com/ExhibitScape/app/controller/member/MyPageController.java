package com.ExhibitScape.app.controller.member;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ExhibitScape.app.dto.member.MemberDetails;
import com.ExhibitScape.app.service.member.MyPageService;

@Controller
@RequestMapping("/myPage")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @GetMapping
    public String myPage(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        if (memberDetails != null) {
            model.addAttribute("member", myPageService.getMemberInfo(memberDetails.getUsername()));
            return "member/myPage";
        } else {
            return "redirect:/login?error";
        }
    }

    @PostMapping("/updateNickname")
    public String updateNickname(@AuthenticationPrincipal MemberDetails memberDetails, @RequestParam("nickname") String nickname) {
        myPageService.updateNickname(memberDetails.getUsername(), nickname);
        return "redirect:/myPage";
    }

    @PostMapping("/updateEmail")
    public String updateEmail(@AuthenticationPrincipal MemberDetails memberDetails, @RequestParam("email") String email) {
        myPageService.updateEmail(memberDetails.getUsername(), email);
        return "redirect:/myPage";
    }

    @PostMapping("/checkDuplicate")
    @ResponseBody
    public boolean checkDuplicate(@RequestParam("field") String field, @RequestParam("value") String value) {
        if (field.equals("nickname")) {
            return !myPageService.isNicknameDuplicate(value);
        } else if (field.equals("email")) {
            return !myPageService.isEmailDuplicate(value);
        }
        return false;
    }
}