package com.ExhibitScape.app.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ExhibitScape.app.dto.member.JoinDTO;
import com.ExhibitScape.app.service.member.JoinService;

import jakarta.validation.Valid;



@Controller
public class JoinController {

	
	@Autowired
	private JoinService joinService;
	
	  @GetMapping("/join")
	        public String joinP(JoinDTO joinDTO, Model model) {
		    model.addAttribute("joinDTO", new JoinDTO());
		    return "/member/join";
		}
	
	
	@PostMapping("/join")       //유효성검사
	public String joinP(@Valid JoinDTO joinDTO, BindingResult bindingResult, Model model) {
		
		System.out.println("joinDTO="+joinDTO);
		//유효성검사
			if(bindingResult.hasErrors()) {
				return "/member/join";     //join.html
			}
		
//			if(!joinDTO.getPassword1().equals(joinDTO.getPassword2())) { //비번과 확인용비번일치여부 체크
//				//BindingResult에 에러발생한필드,에러코드,출력메세지를 보내자
//				//bindingResult.reject("passwordNotMatch", "비밀번호가 일치하지 않습니다");
//				bindingResult.rejectValue("password2", "passwordNotMatch", "비밀번호가 일치하지 않습니다");
//				return "/member/join";     //join.html
//			}
//			
		 try {
//			 
//	            // Member ID 중복 확인
//	            if (joinService.checkDuplicateMemberId(joinDTO.getMemberId())) {
//	                bindingResult.rejectValue("memberId", "duplicateMemberId", "이미 사용하고 있는 ID입니다.");
//	                return "/member/join";
//	            }
//
//	            // Nickname 중복 확인
//	            if (joinService.checkDuplicateNickname(joinDTO.getNickname())) {
//	                bindingResult.rejectValue("nickname", "duplicateNickname", "이미 사용하고 있는 닉네임입니다.");
//	                return "/member/join";
//	            }
//
//	            // Email 중복 확인
//	            if (joinService.checkDuplicateEmail(joinDTO.getEmail())) {
//	                bindingResult.rejectValue("email", "duplicateEmail", "이미 사용하고 있는 이메일입니다.");
//	                return "/member/join";
//	            }
//	            
	            joinService.joinProcess(joinDTO.getMemberId(),
						 joinDTO.getPassword1(), 
						 joinDTO.getNickname(), 
						 joinDTO.getEmail()); 
			
		}catch(DataIntegrityViolationException e) {
			e.printStackTrace(); //개발자를 위한 콘솔 출력
			//BindingResult에 에러코드,출력메세지
			//bindingResult.reject("signupFailed","회원가입 실패");
			//return "/member/join";  //templates폴더/signupform.html문서를 보여줘
		}catch(Exception e) {
			e.printStackTrace(); 
		//	bindingResult.reject("signupFailed", e.getMessage());
		//	return "/member/join";  
		}
	

			return "redirect:/login";

}
}