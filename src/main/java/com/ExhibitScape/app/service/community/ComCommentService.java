package com.ExhibitScape.app.service.community;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.ExhibitScape.app.common.DataNotException;
import com.ExhibitScape.app.domain.community.ComComment;
import com.ExhibitScape.app.domain.community.ComCommentRepository;
import com.ExhibitScape.app.domain.community.Community;
import com.ExhibitScape.app.dto.community.ComCommentDTO;
import com.ExhibitScape.app.dto.community.CommunityDTO;

import jakarta.transaction.Transactional;

@Service("comCommentService")
public class ComCommentService {
	
	@Autowired
	private ComCommentRepository comCommentRepository;

	
	//댓글 등록 처리 
	public ComCommentDTO commentwrite(CommunityDTO communityDTO,String comContent) {
		ComComment comment = new ComComment();
		comment.setComContent(comContent); //답변 내용
		System.out.println(comContent);
		Community community = new Community();
		community.setComId(communityDTO.getComId());
		community.setMemberId(communityDTO.getMemberId());
		community.setComTitle(communityDTO.getComTitle());
		community.setComContent(communityDTO.getComContent());
		community.setComImgSname(communityDTO.getComImgSname());
		community.setComImgPath(communityDTO.getComImgPath());
		community.setComCategory(communityDTO.getComCategory());
		community.setComHits(communityDTO.getComHits());
		community.setComTag(communityDTO.getComTag());
		
		comment.setComId(community);
		
		comCommentRepository.save(comment);
		ComCommentDTO comCommentDTO = convertDTO(comment);
		return comCommentDTO;
		
	}
	
	//댓글 정보 가져오기 
	public ComCommentDTO commentDetail(Integer comComId) {
		Optional<ComComment> oComment = comCommentRepository.findById(comComId);
		if(oComment.isPresent()) {
			
			ComComment comComment = oComment.get();
			ComCommentDTO comCommentDTO = convertDTO(comComment);
			
			return comCommentDTO;
			
		}else {	
			throw new DataNotException("comment not found");
		}
	}
	
	//댓글 수정 처리
	public ComCommentDTO update(Integer comComId,String comContent) {
		//댓글 조회 및 예외 발생
		ComComment comment = comCommentRepository.findById(comComId)
							.orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + comComId));
		
		comment.setComContent(comContent); //답변 내용
		ComComment updateComment = comCommentRepository.save(comment);
		return convertDTO(updateComment);
	}
	
	private ComCommentDTO convertDTO(ComComment comComment) {
		ComCommentDTO dTo = new ComCommentDTO();
		
		dTo.setComComId(comComment.getComComId());
		dTo.setMemberId(comComment.getMemberId());
		dTo.setComContent(comComment.getComContent());
		dTo.setComId(comComment.getComId());
		
		// postDate를 원하는 형식으로 변환하여 설정
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String formattedDate = comComment.getPostDate().format(formatter);
	    dTo.setPostDate(formattedDate);
		
		
		return dTo;
	}

}
