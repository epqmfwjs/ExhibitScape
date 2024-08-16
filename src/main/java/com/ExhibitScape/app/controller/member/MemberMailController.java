package com.ExhibitScape.app.controller.member;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ExhibitScape.app.service.member.MemberMailService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes({"memberId", "email"})
public class MemberMailController {

    @Autowired
    private MemberMailService memberMailService;

    @PostMapping("/member/checkMemberInfo")
    public String checkMemberInfo(@RequestBody Map<String, String> requestData, HttpSession session, Model model) {
        String memberId = requestData.get("memberId");
        String email = requestData.get("email");
        
        boolean isMatch = memberMailService.checkMemberInfo(memberId, email);
        
        if (isMatch) {
            session.setAttribute("memberId", memberId);
            session.setAttribute("email", email);
            return "redirect:/sendEmail";
        } else {
            model.addAttribute("message", "입력한 정보와 일치하는 회원이 없습니다.");
            return "login";
        }
    }
    
    @GetMapping("/sendEmail")
    public String sendEmail(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        String email = (String) session.getAttribute("email");

        if (memberId != null && email != null) {
            boolean isSent = memberMailService.sendTemporaryPassword(memberId, email);
            if (isSent) {
                model.addAttribute("message", "회원 이메일로 임시 비밀번호를 발송하였습니다.");
            } else {
                model.addAttribute("message", "임시 비밀번호 발송에 실패했습니다.");
            }
        } else {
            model.addAttribute("message", "잘못된 접근입니다.");
        }

        return "login";
    }
}