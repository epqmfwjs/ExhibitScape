package com.ExhibitScape.app.controller.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ExhibitScape.app.domain.community.ComComment;
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
	@PostMapping("/write/{comId}")
	@ResponseBody
	public ComCommentDTO commentwrite(@PathVariable("comId") Integer comId, @RequestParam("comContent") String comContent) {
		//comComment 엔터티의 community community필드를 설정하기위한 질문
		CommunityDTO communityDTO = communityService.communityDetail(comId);		
		ComCommentDTO comCommentDTO = comCommentService.commentwrite(communityDTO,comContent);		
		return comCommentDTO;
		
	}

	
	//댓글 수정 처리
	@PostMapping("/update/{comComId}")
	@ResponseBody
	public ResponseEntity<ComCommentDTO> update(@PathVariable("comComId") Integer comComId, @RequestParam("comContent") String comContent){
		
		//service에게 위임
        ComCommentDTO updateDTO = comCommentService.update(comComId, comContent);
        return ResponseEntity.status(HttpStatus.OK).body(updateDTO);
	}

}
