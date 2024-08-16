package com.ExhibitScape.app.controller.member;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.dto.member.MemberDetails;
import com.ExhibitScape.app.service.member.MemberDeleteService; // 수정된 부분
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/memberDelete")
public class MemberDeleteController {

    @Autowired
    private MemberDeleteService memberDeleteService; // 수정된 부분

    @GetMapping
    public String memberDelete(@AuthenticationPrincipal MemberDetails memberDetails) {
        if (memberDetails != null) {
            MemberDomain member = memberDeleteService.findByMemberId(memberDetails.getUsername()); // 수정된 부분
            if (member != null) {
                memberDeleteService.deleteMember(member.getMemberNo()); // 수정된 부분
            }
        }
        return "redirect:/logout"; // 로그아웃 페이지로 이동
    }
}
