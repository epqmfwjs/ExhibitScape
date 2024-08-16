package com.ExhibitScape.app.service.gallery;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ExhibitScape.app.domain.gallery.GalleryEntity;
import com.ExhibitScape.app.domain.gallery.GalleryFileEntity;
import com.ExhibitScape.app.domain.gallery.GalleryFileRepository;
import com.ExhibitScape.app.domain.gallery.GalleryRepository;
import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.dto.gallery.GalleryDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GalleryService {

	private final GalleryRepository galleryRepository;
	private final GalleryFileRepository galleryFileRepository;
	private final GalCommentService galCommentService;

	public void save(GalleryDTO galleryDTO) throws IOException {
	    if (galleryDTO.getGalleryFile() == null || galleryDTO.getGalleryFile().isEmpty()) {
	        // 이미지를 첨부하지 않은 경우
	        GalleryEntity galleryEntity = GalleryEntity.toSaveEntity(galleryDTO);
	        galleryRepository.save(galleryEntity);
		} else {
			//이미지를 첨부한 경우
			GalleryEntity galleryEntity = GalleryEntity.toSaveFileEntity(galleryDTO);
			Long savedId = galleryRepository.save(galleryEntity).getGalId();
			GalleryEntity gallery = galleryRepository.findById(savedId).get();

			for (MultipartFile galleryFile : galleryDTO.getGalleryFile()) {

				// MultipartFile galleryFile = galleryDTO.getGalleryFile(); // 이미지 한개
				String galImgOfile = galleryFile.getOriginalFilename();
				String galImgSfile = System.currentTimeMillis() + "_" + galImgOfile;
				String savePath = "C:/imgs/" + galImgSfile;
				galleryFile.transferTo(new File(savePath));

				GalleryFileEntity galleryFileEntity = GalleryFileEntity.toGalleryFileEntity(gallery, galImgOfile,
						galImgSfile);
				galleryFileRepository.save(galleryFileEntity);
			}
		}
	}

//	@Transactional
//	public List<GalleryDTO> findAll() {
//		List<GalleryEntity> galleryEntityList = galleryRepository.findAll();
//		List<GalleryDTO> galleryDTOList = new ArrayList<>();
//		for (GalleryEntity galleryEntity : galleryEntityList) {
//			galleryDTOList.add(GalleryDTO.toGalleryDTO(galleryEntity));
//		}
//		return galleryDTOList;
//	}

	@Transactional
	public Page<GalleryDTO> findAll(Pageable pageable) {
	    Page<GalleryEntity> galleryEntityPage = galleryRepository.findAll(pageable);
	    return galleryEntityPage.map(GalleryDTO::toGalleryDTO);
	}
	
	@Transactional
	public Page<GalleryDTO> search(String searchOption, String searchWord, Pageable pageable) {
	    Page<GalleryEntity> galleryEntities;

	    switch (searchOption) {
	        case "title":
	            galleryEntities = galleryRepository.findByGalTitleContaining(searchWord, pageable);
	            break;
	        case "location":
	            galleryEntities = galleryRepository.findByGalLocationContaining(searchWord, pageable);
	            break;
	        case "address":
	            galleryEntities = galleryRepository.findByGalAddressContaining(searchWord, pageable);
	            break;
	        default:
	            galleryEntities = galleryRepository.findAll(pageable);
	            break;
	    }

	    return galleryEntities.map(GalleryDTO::toGalleryDTO);
	}
	
	@Transactional
	public void updateHits(Long id) {
		galleryRepository.updateHits(id);
	}

	@Transactional
	public GalleryDTO findById(Long id) {
	    GalleryEntity galleryEntity = galleryRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid gallery ID: " + id));
	    GalleryDTO galleryDTO = GalleryDTO.toGalleryDTO(galleryEntity);
	    galleryDTO.setMemberId(galleryEntity.getMemberId());
	    return galleryDTO;
	}

	public GalleryDTO update(GalleryDTO galleryDTO) throws IOException {
		GalleryEntity galleryEntity = galleryRepository.findById(galleryDTO.getGalId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid gallery ID: " + galleryDTO.getGalId()));

		// 갤러리 엔티티의 필드 값을 업데이트
		galleryEntity.setGalTitle(galleryDTO.getGalTitle());
		galleryEntity.setGalPeriod(galleryDTO.getGalPeriod());
		galleryEntity.setGalLocation(galleryDTO.getGalLocation());
		galleryEntity.setGalAddress(galleryDTO.getGalAddress());
		galleryEntity.setGalGenre(galleryDTO.getGalGenre());
		galleryEntity.setGalInfo(galleryDTO.getGalInfo());
		// Null 체크 및 파일 저장
		if (galleryDTO.getGalleryFile() != null && !galleryDTO.getGalleryFile().isEmpty()) {
			// 기존 파일들을 처리하기 위해 갤러리 파일 엔티티를 모두 삭제합니다.
			List<GalleryFileEntity> existingFiles = galleryFileRepository.findByGalleryEntity(galleryEntity);
			galleryFileRepository.deleteAll(existingFiles);

			for (MultipartFile galleryFile : galleryDTO.getGalleryFile()) {
				if (!galleryFile.isEmpty()) {
					String galImgOfile = galleryFile.getOriginalFilename();
					String galImgSfile = System.currentTimeMillis() + "_" + galImgOfile;
					String savePath = "C:/imgs/" + galImgSfile;
					galleryFile.transferTo(new File(savePath));

					GalleryFileEntity galleryFileEntity = GalleryFileEntity.toGalleryFileEntity(galleryEntity,
							galImgOfile, galImgSfile);
					galleryFileRepository.save(galleryFileEntity);
				}
			}
		}

		galleryRepository.save(galleryEntity); // 변경된 내용을 저장합니다.
		return GalleryDTO.toGalleryDTO(galleryEntity);
	}

	public void delete(Long id) {
		galleryRepository.deleteById(id);
	}

	
	@Transactional
	public List<GalleryDTO> search(String searchOption, String searchWord) {
		List<GalleryEntity> galleryEntities;

		// 검색 옵션에 따라 검색 수행
		switch (searchOption) {
		case "title":
			galleryEntities = galleryRepository.findByGalTitleContaining(searchWord);
			break;
		case "location":
			galleryEntities = galleryRepository.findByGalLocationContaining(searchWord);
			break;
		case "address":
			galleryEntities = galleryRepository.findByGalAddressContaining(searchWord);
			break;
		default:
			// 검색 옵션이 없는 경우 빈 리스트 반환
			galleryEntities = new ArrayList<>();
			break;
		}

		// 검색 결과를 DTO로 변환하여 반환
		List<GalleryDTO> galleryDTOList = new ArrayList<>();
		for (GalleryEntity galleryEntity : galleryEntities) {
			galleryDTOList.add(GalleryDTO.toGalleryDTO(galleryEntity));
		}

		return galleryDTOList;
	}

	@Transactional
	public List<GalleryDTO> findByCategory(String category) {
		List<GalleryEntity> galleryEntities = galleryRepository.findByGalCategoryContaining(category);
		return galleryEntities.stream().map(GalleryDTO::toGalleryDTO).collect(Collectors.toList());
		/*
		 * stream의 map()은 괄호 안의 동작을 하나하나 수행하고 나서 가공된 결과를 반환 collect(Collectors.toList()
		 * : 다시 stream을 List 형태로 변환
		 */
	}

	public List<GalleryDTO> searchByDateRange(String startDateRange, String endDateRange) {
	    List<GalleryEntity> galleryEntities = galleryRepository.findByGalPeriodBetween(startDateRange, endDateRange);
	    return galleryEntities.stream()
	            .map(GalleryDTO::toGalleryDTO)
	            .collect(Collectors.toList());
	}
	
//	@Transactional
//	public Page<GalleryDTO> searchByDateRange(String startDateRange, String endDateRange, Pageable pageable) {
//	    Page<GalleryEntity> galleryEntities = galleryRepository.findByGalPeriodBetween(startDateRange, endDateRange, pageable);
//	    return galleryEntities.map(GalleryDTO::toGalleryDTO);
//	}
	
	//admin관련 추가
    public int getTotalCount() {
        return (int) galleryRepository.count();
    }
}