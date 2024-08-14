package com.ExhibitScape.app.controller.member;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ExhibitScape.app.dto.member.MemberDetails;
import com.ExhibitScape.app.service.member.SecurityService;

@Controller
public class MainController {

		
	
	@Autowired
	SecurityService SecurityService;
	
	@GetMapping("/")
	public String mainP(Model model) {
		
		
	    
	 // 사용자 아이디와 권한을 초기화
	    String memberId=SecurityService.userId();
	    String role=SecurityService.userGrade();
	   
	    model.addAttribute("memberId",memberId);
	    model.addAttribute("role",role);
		return "/member/main";
	}
}
