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
import org.springframework.web.bind.annotation.RequestMapping;

import com.ExhibitScape.app.dto.member.MemberDetails;
import com.ExhibitScape.app.service.member.SecurityService;

@Controller
public class MainController {

		
	
	@Autowired
	SecurityService SecurityService;
	
	@RequestMapping("/")
	public String mainP() {
		
		
		return "redirect:/scheduleBoard/list";
	}
}
