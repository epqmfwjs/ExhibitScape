package com.ExhibitScape.app.domain.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryLikeRepository extends JpaRepository<GalleryLikeEntity, Long> {

	GalleryLikeEntity findByIdAndMemberId(Long id, String memberId);

	int countByGalleryGalIdAndLove(Long galId, boolean love);
}
