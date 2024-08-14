package com.ExhibitScape.app.service.gallery;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ExhibitScape.app.domain.gallery.GalCommentEntity;
import com.ExhibitScape.app.domain.gallery.GalCommentRepository;
import com.ExhibitScape.app.domain.gallery.GalleryEntity;
import com.ExhibitScape.app.domain.gallery.GalleryRepository;
import com.ExhibitScape.app.dto.gallery.GalCommentDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GalCommentService {

	private final GalCommentRepository galCommentRepository;
	private final GalleryRepository galleryRepository;

	public Long save(GalCommentDTO galCommentDTO) {
        Long galId = galCommentDTO.getGalId();
		if (galId == null) {
			throw new IllegalArgumentException("Gallery ID must not be null");
		}

		GalleryEntity galleryEntity = galleryRepository.findById(galId)
				.orElseThrow(() -> new IllegalArgumentException("Gallery with ID " + galId + " does not exist"));

	    GalCommentEntity galCommentEntity = GalCommentEntity.toSaveEntity(galCommentDTO, galleryEntity);
	    GalCommentEntity savedEntity = galCommentRepository.save(galCommentEntity);
	    return savedEntity.getId();
	}

//	public List<GalCommentDTO> findAll(Long galleryId) {
//        GalleryEntity galleryEntity = galleryRepository.findById(galleryId)
//                .orElseThrow(() -> new IllegalArgumentException("Gallery with ID " + galleryId + " does not exist"));
//
//        List<GalCommentEntity> galCommentEntityList = galCommentRepository
//                .findAllByGalleryEntityOrderByIdDesc(galleryEntity);
//
//        List<GalCommentDTO> galCommentDTOList = new ArrayList<>();
//        for (GalCommentEntity galCommentEntity : galCommentEntityList) {
//            GalCommentDTO galCommentDTO = GalCommentDTO.toGalCommentDTO(galCommentEntity, galleryId);
//            galCommentDTOList.add(galCommentDTO);
//        }
//        return galCommentDTOList;
//	}
	
	public List<GalCommentDTO> findAll(Long galId) {
	    if (galId == null || galId < 1) {
	        throw new IllegalArgumentException("Invalid gallery ID: " + galId);
	    }
	    GalleryEntity galleryEntity = galleryRepository.findById(galId)
	            .orElseThrow(() -> new IllegalArgumentException("Gallery with ID " + galId + " does not exist"));
	    List<GalCommentEntity> galCommentEntityList = galCommentRepository.findAllByGalleryEntityOrderByIdDesc(galleryEntity);
	    return galCommentEntityList.stream()
	            .map(galCommentEntity -> GalCommentDTO.toGalCommentDTO(galCommentEntity, galId))
	            .collect(Collectors.toList());
	}
	
//	
	public GalCommentDTO findById(Long id) {
	    GalCommentEntity galCommentEntity = galCommentRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Comment with ID " + id + " does not exist"));
	    return GalCommentDTO.toGalCommentDTO(galCommentEntity, galCommentEntity.getGalleryEntity().getGalId());
	}

    public void update(Long id, GalCommentDTO galCommentDTO) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("Invalid comment ID: " + id);
        }
        GalCommentEntity galCommentEntity = galCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with ID " + id + " does not exist"));
        galCommentEntity.setGalComContent(galCommentDTO.getGalComContent());
        galCommentRepository.save(galCommentEntity);
    }
    
    public Long delete(Long id) {
        GalCommentEntity galCommentEntity = galCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment with ID " + id + " does not exist"));
        Long galId = galCommentEntity.getGalleryEntity().getGalId();
        galCommentRepository.deleteById(id);
        return galId;
    }
}
