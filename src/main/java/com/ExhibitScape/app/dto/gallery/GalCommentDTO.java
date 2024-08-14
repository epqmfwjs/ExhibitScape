package com.ExhibitScape.app.dto.gallery;

import java.time.LocalDateTime;

import com.ExhibitScape.app.domain.gallery.GalCommentEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GalCommentDTO {
	
	
	private Long id;
	
	private String memberId;
    
	private String galComContent;
	
	private Long galId;
	
	private LocalDateTime commentCreatedTime;
	
	
	public static GalCommentDTO toGalCommentDTO(GalCommentEntity galCommentEntity, Long galId) {
		GalCommentDTO galCommentDTO = new GalCommentDTO();
		galCommentDTO.setId(galCommentEntity.getId());
		galCommentDTO.setMemberId(galCommentEntity.getMemberId());
		galCommentDTO.setGalComContent(galCommentEntity.getGalComContent());
		galCommentDTO.setCommentCreatedTime(galCommentEntity.getCreatedTime());
		//galCommentDTO.setGalId(galCommentEntity.getGalleryEntity().getGalId());
		galCommentDTO.setGalId(galId);
		return galCommentDTO;
	}
	
	
}
