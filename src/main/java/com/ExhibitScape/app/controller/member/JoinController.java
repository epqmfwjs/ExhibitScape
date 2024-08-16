package com.ExhibitScape.app.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ExhibitScape.app.dto.member.JoinDTO;
import com.ExhibitScape.app.service.member.JoinService;

import jakarta.validation.Valid;

@Controller
public class JoinController {

    @Autowired
    private JoinService joinService;

    @GetMapping("/join")
    public String joinP(JoinDTO joinDTO) {
        return "member/join";
    }

    @PostMapping("/join")
    public String joinP(@ModelAttribute("joinDTO") @Valid JoinDTO joinDTO, BindingResult bindingResult, Model model) {
        System.out.println("joinDTO=" + joinDTO);

        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        if (!joinDTO.getPassword1().equals(joinDTO.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordNotMatch", "비밀번호가 일치하지 않습니다");
            model.addAttribute("joinDTO", joinDTO);
            return "member/join";
        }

        try {
            if (joinService.checkDuplicateMemberId(joinDTO.getMemberId())) {
                bindingResult.rejectValue("memberId", "duplicateMemberId", "이미 사용하고 있는 ID입니다.");
                model.addAttribute("joinDTO", joinDTO);
                return "member/join";
            }

            if (joinService.checkDuplicateNickname(joinDTO.getNickname())) {
                bindingResult.rejectValue("nickname", "duplicateNickname", "이미 사용하고 있는 닉네임입니다.");
                model.addAttribute("joinDTO", joinDTO);
                return "member/join";
            }

            if (joinService.checkDuplicateEmail(joinDTO.getEmail())) {
                bindingResult.rejectValue("email", "duplicateEmail", "이미 사용하고 있는 이메일입니다.");
                model.addAttribute("joinDTO", joinDTO);
                return "member/join";
            }

            joinService.joinProcess(joinDTO.getMemberId(), joinDTO.getPassword1(), joinDTO.getNickname(), joinDTO.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("joinDTO", joinDTO);
            return "member/join";
        }

        return "redirect:/login";
    }
}