package com.ExhibitScape.app.dto.gallery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ExhibitScape.app.domain.gallery.GalleryEntity;
import com.ExhibitScape.app.domain.gallery.GalleryFileEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryDTO {

	private Long galId;
	private String memberId;
	private String galTitle;
	private String galInfo;
	private String galPeriod;
	private String galAddress;
	private String galGenre;
	private String galLocation;
	private int galHits;
	private LocalDateTime galPostDate;
	private LocalDateTime galUpdateDate;

	private String galCategory;

	private List<MultipartFile> galleryFile; // 여러 파일을 받기 위한 배열 추가
	private List<String> galImgOfile;
	private List<String> galImgSfile;
	private int fileAttached;

	public GalleryDTO(Long galId, String galTitle, String galAddress, String galLocation, int galHits,
			LocalDateTime galPostDate) {
		super();
		this.galId = galId;
		this.galTitle = galTitle;
		this.galAddress = galAddress;
		this.galLocation = galLocation;
		this.galHits = galHits;
		this.galPostDate = galPostDate;
	}

	public static GalleryDTO toGalleryDTO(GalleryEntity galleryEntity) {
		GalleryDTO galleryDTO = new GalleryDTO();
		galleryDTO.setGalId(galleryEntity.getGalId());
		galleryDTO.setGalTitle(galleryEntity.getGalTitle());
		galleryDTO.setGalInfo(galleryEntity.getGalInfo());
		galleryDTO.setGalPeriod(galleryEntity.getGalPeriod());
		galleryDTO.setGalAddress(galleryEntity.getGalAddress());
		galleryDTO.setGalGenre(galleryEntity.getGalGenre());
		galleryDTO.setGalLocation(galleryEntity.getGalLocation());
		galleryDTO.setGalHits(galleryEntity.getGalHits());
		galleryDTO.setGalPostDate(galleryEntity.getCreatedTime());
		galleryDTO.setGalUpdateDate(galleryEntity.getUpdatedTime());

		
		if (galleryEntity.getFileAttached() == 0) {
			galleryDTO.setFileAttached(galleryEntity.getFileAttached()); // 0
		} else {
			List<String> galImgOfileList = new ArrayList<>();
			List<String> galImgSfileList = new ArrayList<>();

			galleryDTO.setFileAttached(galleryEntity.getFileAttached()); // 1

			for (GalleryFileEntity galleryFileEntity : galleryEntity.getGalleryFileEntityList()) {
				galImgOfileList.add(galleryFileEntity.getGalImgOfile());
				galImgSfileList.add(galleryFileEntity.getGalImgSfile());
			}
			galleryDTO.setGalImgOfile(galImgOfileList);
			galleryDTO.setGalImgSfile(galImgSfileList);
		}
		return galleryDTO;
	}


		
}