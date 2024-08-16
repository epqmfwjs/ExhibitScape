package com.ExhibitScape.app.controller.community;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.dto.community.ComCommentDTO;
import com.ExhibitScape.app.dto.community.CommunityDTO;
import com.ExhibitScape.app.service.community.ComCommentService;
import com.ExhibitScape.app.service.community.CommunityService;

@Controller
@RequestMapping("/exhibitscape/comment")
public class ComCommmentController {
	
	@Autowired
	private CommunityService communityService;
	
	@Autowired
	private ComCommentService comCommentService;
	
	//댓글 등록 처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/write/{comId}")
	@ResponseBody
	public ComCommentDTO commentwrite(@PathVariable("comId") Integer comId, @RequestParam("comContent") String comContent
										,Principal principal) {
		
		// 랜덤 이미지 파일 이름 생성 및 DTO에 설정
		String randomImageFileName = ImageUtil.getRandomImageFileName();
		
		//comComment 엔터티의 community community필드를 설정하기위한 질문
		String memberId = principal.getName();	
		CommunityDTO communityDTO = communityService.communityDetailWithoutLike(comId);		
		ComCommentDTO comCommentDTO = comCommentService.commentwrite(communityDTO,comContent,memberId,randomImageFileName);		
		System.out.println(comCommentDTO);
		return comCommentDTO;
		
	}

	
	//댓글 수정 처리
	@PostMapping("/update/{comComId}")
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	public ResponseEntity<ComCommentDTO> update(@PathVariable("comComId") Integer comComId, @RequestParam("comContent") String comContent
												,Principal principal){
		
		//service에게 위임
        ComCommentDTO updateDTO = comCommentService.update(comComId, comContent);
        return ResponseEntity.status(HttpStatus.OK).body(updateDTO);
	}
	
	@PostMapping("/delete/{comComId}")
	@PreAuthorize("isAuthenticated()")
	//댓글 삭제 처리
	public ResponseEntity<String> delete(@PathVariable("comComId") Integer comComId,Principal principal){

		try {
	        comCommentService.delete(comComId);
	        return ResponseEntity.ok("success");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("fail");
	    }
	}

}
