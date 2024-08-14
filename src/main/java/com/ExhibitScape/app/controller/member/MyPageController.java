package com.ExhibitScape.app.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {
	@GetMapping("/myPage")
	public String adminP() {
		
		return "/member/myPage";
	}
}
