package com.ExhibitScape.app.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

	@GetMapping("/admin")
	public String adminP() {
		
		return "/member/admin";
	}
}
