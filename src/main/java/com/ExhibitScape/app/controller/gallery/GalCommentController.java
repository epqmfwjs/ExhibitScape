package com.ExhibitScape.app.controller.gallery;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ExhibitScape.app.dto.gallery.GalCommentDTO;
import com.ExhibitScape.app.dto.member.MemberDetails;
import com.ExhibitScape.app.service.gallery.GalCommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class GalCommentController {

	private final GalCommentService galCommentService;

	@GetMapping("/list/{galId}")
	public ResponseEntity<List<GalCommentDTO>> getCommentList(@PathVariable("galId") Long galId) {
		List<GalCommentDTO> commentList = galCommentService.findAll(galId);
		return ResponseEntity.ok(commentList);
	}

	@PostMapping("/save")
	public ResponseEntity<?> save(@ModelAttribute GalCommentDTO galCommentDTO, Model model, Authentication authentication) {
	    if (authentication == null || !authentication.isAuthenticated()) {
	        return new ResponseEntity<>("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
	    }

	    System.out.println("galCommentDTO = " + galCommentDTO);

	    String memberId = null;
	    String nickname = null;
	    if (authentication != null && authentication.isAuthenticated()) {
	        MemberDetails userDetails = (MemberDetails) authentication.getPrincipal();
	        memberId = userDetails.getUsername();
	        nickname = userDetails.getNickname();
	        
	        System.out.println("확인용: "+memberId);
	        System.out.println("확인용: "+nickname);
	        
	        // galCommentDTO 객체에 memberId 설정
	        galCommentDTO.setMemberId(memberId);
	    }
	    Long saveResult = galCommentService.save(galCommentDTO);
	    
	    if (saveResult != null) {
	        // 작성 성공하면 댓글 목록 가져와서 리턴 //댓글목록: 해당 게시글의 댓글 전체
	        List<GalCommentDTO> galCommentDTOList = galCommentService.findAll(galCommentDTO.getGalId());
	        if (memberId != null) {
	            model.addAttribute("memberId", memberId);
	        }
	        return new ResponseEntity<>(galCommentDTOList, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("해당 게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND);
	    }
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<GalCommentDTO> getComment(@PathVariable("id") Long id) {
	    GalCommentDTO galCommentDTO = galCommentService.findById(id);
	    return ResponseEntity.ok(galCommentDTO);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<List<GalCommentDTO>> update(@PathVariable("id") Long id, @RequestBody GalCommentDTO galCommentDTO) {
	    if (id == null || id < 1) {
	        //return ResponseEntity.badRequest().body(null);
	        return ResponseEntity.badRequest().build();
	    }
	    galCommentService.update(id, galCommentDTO);
	    List<GalCommentDTO> updatedComments = galCommentService.findAll(galCommentDTO.getGalId());
	    return ResponseEntity.ok(updatedComments);
	}
	
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<GalCommentDTO>> delete(@PathVariable("id") Long id) {
        Long galId = galCommentService.delete(id);
        List<GalCommentDTO> updatedComments = galCommentService.findAll(galId);
        return ResponseEntity.ok(updatedComments);
    }
    
}

