package com.ExhibitScape.app.domain.gallery;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.dto.gallery.GalleryDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "gallery_table")
public class GalleryEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private Long galId;

	@Column
	private String memberId;

	@Column(nullable = false)
	private String galTitle;

	@Column(length = 1000, nullable = false)
	private String galInfo;

	@Column
	private String galPeriod;

	@Column
	private String galAddress;

	@Column
	private String galGenre;

	@Column
	private String galLocation;

	@Column
	private int galHits;
	
	@Column
	private int fileAttached;

    @Column(name = "gal_category")
    private String galCategory;

	@OneToMany(mappedBy = "galleryEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GalleryFileEntity> galleryFileEntityList = new ArrayList<>();
	
	@OneToMany(mappedBy = "galleryEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<GalCommentEntity> galCommentEntityList = new ArrayList<>();
	
	@ManyToOne
    @JoinColumn(name = "member_id")
    private MemberDomain member;
	
	public GalleryEntity() {
	}
	
    public GalleryEntity(Long galId) {
        this.galId = galId;
    }

	public static GalleryEntity toSaveEntity(GalleryDTO galleryDTO) {
		GalleryEntity galleryEntity = new GalleryEntity();
		galleryEntity.setMemberId(galleryDTO.getMemberId());
		galleryEntity.setGalTitle(galleryDTO.getGalTitle());
		galleryEntity.setGalInfo(galleryDTO.getGalInfo());
		galleryEntity.setGalPeriod(galleryDTO.getGalPeriod());
		galleryEntity.setGalAddress(galleryDTO.getGalAddress());
		galleryEntity.setGalGenre(galleryDTO.getGalGenre());
		galleryEntity.setGalLocation(galleryDTO.getGalLocation());
		galleryEntity.setGalHits(0);
		galleryEntity.setFileAttached(0);
		
		return galleryEntity;
	}

	public static GalleryEntity toUpdateEntity(GalleryDTO galleryDTO) {
		GalleryEntity galleryEntity = new GalleryEntity();
		galleryEntity.setMemberId(galleryDTO.getMemberId());
		galleryEntity.setGalTitle(galleryDTO.getGalTitle());
		galleryEntity.setGalInfo(galleryDTO.getGalInfo());
		galleryEntity.setGalPeriod(galleryDTO.getGalPeriod());
		galleryEntity.setGalAddress(galleryDTO.getGalAddress());
		galleryEntity.setGalGenre(galleryDTO.getGalGenre());
		galleryEntity.setGalLocation(galleryDTO.getGalLocation());
		galleryEntity.setGalHits(galleryDTO.getGalHits());
		return galleryEntity;
	}

	public static GalleryEntity toSaveFileEntity(GalleryDTO galleryDTO) {
		GalleryEntity galleryEntity = new GalleryEntity();
		galleryEntity.setMemberId(galleryDTO.getMemberId());
		galleryEntity.setGalTitle(galleryDTO.getGalTitle());
		galleryEntity.setGalInfo(galleryDTO.getGalInfo());
		galleryEntity.setGalPeriod(galleryDTO.getGalPeriod());
		galleryEntity.setGalAddress(galleryDTO.getGalAddress());
		galleryEntity.setGalGenre(galleryDTO.getGalGenre());
		galleryEntity.setGalLocation(galleryDTO.getGalLocation());
		galleryEntity.setGalHits(0);
		//galleryEntity.setFileAttached(1);//파일있음
		galleryEntity.setFileAttached(galleryDTO.getGalleryFile() != null && !galleryDTO.getGalleryFile().isEmpty() ? 1 : 0);
		return galleryEntity;	
		}

}
