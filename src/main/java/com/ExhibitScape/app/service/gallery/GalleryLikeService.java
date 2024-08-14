package com.ExhibitScape.app.service.gallery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.domain.gallery.GalleryEntity;
import com.ExhibitScape.app.domain.gallery.GalleryLikeEntity;
import com.ExhibitScape.app.domain.gallery.GalleryLikeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GalleryLikeService {
//    private final GalleryLikeRepository galleryLikeRepository;
//    private final GalleryRepository galleryRepository;

//    public GalleryLikeDTO save(Long galId, String memberId, Boolean isLike) {
//        GalleryEntity gallery = galleryRepository.findById(galId)
//                .orElseThrow(() -> new IllegalArgumentException("Gallery with ID " + galId + " does not exist"));
//        GalleryLikeEntity galleryLike = new GalleryLikeEntity();
//        galleryLike.setGallery(gallery);
//        galleryLike.setMemberId(memberId);
//        galleryLike.setIsLike(isLike);
//        GalleryLikeEntity savedEntity = galleryLikeRepository.save(galleryLike);
//        return GalleryLikeDTO.toGalleryLikeDTO(savedEntity);
//    }
//
//    public GalleryLikeDTO findByGalleryAndMember(Long galId, String memberId) {
//        GalleryEntity gallery = galleryRepository.findById(galId)
//                .orElseThrow(() -> new IllegalArgumentException("Gallery with ID " + galId + " does not exist"));
//        GalleryLikeEntity galleryLike = galleryLikeRepository.findByGalleryAndMemberId(gallery, memberId)
//                .orElse(null);
//        return galleryLike != null ? GalleryLikeDTO.toGalleryLikeDTO(galleryLike) : null;
//    }
//
//    public List<GalleryLikeDTO> findLikesByGallery(Long galId, Boolean isLike) {
//        GalleryEntity gallery = galleryRepository.findById(galId)
//                .orElseThrow(() -> new IllegalArgumentException("Gallery with ID " + galId + " does not exist"));
//        List<GalleryLikeEntity> galleryLikes = galleryLikeRepository.findAllByGalleryAndIsLikeOrderByIdDesc(gallery, isLike);
//        return galleryLikes.stream()
//                .map(GalleryLikeDTO::toGalleryLikeDTO)
//                .collect(Collectors.toList());
//    }
    
    @Autowired
    private GalleryLikeRepository galleryLikeRepository;

    @Transactional
    public int likeGallery(Long galId, String memberId) {
        // 해당 갤러리와 사용자에 대한 좋아요 정보를 찾습니다.
        GalleryLikeEntity galleryLike = galleryLikeRepository.findByIdAndMemberId(galId, memberId);

        if (galleryLike == null) {
            // 해당 갤러리에 사용자가 좋아요를 누른 적이 없으면 새로운 좋아요 정보를 생성합니다.
            galleryLike = new GalleryLikeEntity();
            galleryLike.setGallery(new GalleryEntity(galId)); // Assuming GalleryEntity constructor accepts galleryId
            galleryLike.setMemberId(memberId);
            galleryLike.setLove(true);
            galleryLikeRepository.save(galleryLike);
        } else {
            // 사용자가 이미 좋아요를 눌렀다면 좋아요 상태를 변경합니다.
            galleryLike.setLove(!galleryLike.getLove());
            galleryLikeRepository.save(galleryLike);
        }
        // 좋아요 수를 업데이트하고 반환합니다
        return galleryLikeRepository.countByGalleryGalIdAndLove(galId, true);
    }
    
}
