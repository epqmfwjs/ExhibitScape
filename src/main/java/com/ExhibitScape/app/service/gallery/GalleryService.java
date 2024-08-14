package com.ExhibitScape.app.service.gallery;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ExhibitScape.app.domain.gallery.GalleryEntity;
import com.ExhibitScape.app.domain.gallery.GalleryFileEntity;
import com.ExhibitScape.app.domain.gallery.GalleryFileRepository;
import com.ExhibitScape.app.domain.gallery.GalleryRepository;
import com.ExhibitScape.app.dto.gallery.GalleryDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GalleryService {

	private final GalleryRepository galleryRepository;
	private final GalleryFileRepository galleryFileRepository;

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

	@Transactional
	public List<GalleryDTO> findAll() {
		List<GalleryEntity> galleryEntityList = galleryRepository.findAll();
		List<GalleryDTO> galleryDTOList = new ArrayList<>();
		for (GalleryEntity galleryEntity : galleryEntityList) {
			galleryDTOList.add(GalleryDTO.toGalleryDTO(galleryEntity));
		}
		return galleryDTOList;
	}

	@Transactional
	public void updateHits(Long id) {
		galleryRepository.updateHits(id);
	}

//	@Transactional
//	public GalleryDTO findById(Long id) {
//		Optional<GalleryEntity> optionalGalleryEntity = galleryRepository.findById(id);
//		if (optionalGalleryEntity.isPresent()) {
//			GalleryEntity galleryEntity = optionalGalleryEntity.get();
//			GalleryDTO galleryDTO = GalleryDTO.toGalleryDTO(galleryEntity);
//			return galleryDTO;
//		} else {
//			return null;
//		}
//	}

	@Transactional
	public GalleryDTO findById(Long id) {
		GalleryEntity galleryEntity = galleryRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid gallery ID: " + id));
		return GalleryDTO.toGalleryDTO(galleryEntity);
	}

//	public GalleryDTO update(GalleryDTO galleryDTO) {
//		GalleryEntity galleryEntity = GalleryEntity.toUpdateEntity(galleryDTO);
//		galleryRepository.save(galleryEntity);
//		return findById(galleryDTO.getGalId());
//	}
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

//	public Page<GalleryDTO> paging(Pageable pageable) {
//		int page = pageable.getPageNumber() - 1;
//        int pageLimit = 5; // 한 페이지에 보여줄 글 갯수
//        Page<GalleryEntity> galleryEntities =
//                galleryRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
//	
//        Page<GalleryDTO> galleryDTOs = galleryEntities.map(gallery -> new GalleryDTO(gallery.getGalId(), gallery.getGalTitle(), gallery.getGalLocation(), gallery.getGalAddress(), 
//        		gallery.getCreatedTime(), gallery.getGalHits()));
//        return galleryDTOs;
//	}

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

	public List<GalleryEntity> searchGalleryByDateRange(String startDate, String endDate) {
	    return galleryRepository.findByGalPeriodBetween(startDate, endDate);
	}
   
}