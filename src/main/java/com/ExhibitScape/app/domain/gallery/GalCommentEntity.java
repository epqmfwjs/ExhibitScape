package com.ExhibitScape.app.domain.gallery;

import java.time.LocalDateTime;

import com.ExhibitScape.app.dto.gallery.GalCommentDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class GalCommentEntity extends BaseEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column
	private String memberId;
	
	@Column
	private String galComContent;
	
	@Column
	private LocalDateTime commentCreatedTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gallery_galId")
	private GalleryEntity galleryEntity;
	
	public static GalCommentEntity toSaveEntity(GalCommentDTO galCommentDTO, GalleryEntity galleryEntity) {
		GalCommentEntity galCommentEntity = new GalCommentEntity();
		galCommentEntity.setMemberId(galCommentDTO.getMemberId());
		galCommentEntity.setGalComContent(galCommentDTO.getGalComContent());
		galCommentEntity.setCommentCreatedTime(LocalDateTime.now());
		galCommentEntity.setGalleryEntity(galleryEntity);
		return galCommentEntity; 
	}

}
