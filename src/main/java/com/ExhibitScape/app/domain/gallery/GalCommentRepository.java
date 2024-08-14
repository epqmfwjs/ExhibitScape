package com.ExhibitScape.app.domain.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GalCommentRepository extends JpaRepository<GalCommentEntity, Long> {

	List<GalCommentEntity> findAllByGalleryEntityOrderByIdDesc(GalleryEntity galleryEntity);

	Optional<GalCommentEntity> findById(Long id);

	GalCommentEntity save(GalCommentEntity galCommentEntity);

	void deleteById(Long id);

}
