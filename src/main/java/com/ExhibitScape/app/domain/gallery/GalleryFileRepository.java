package com.ExhibitScape.app.domain.gallery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface GalleryFileRepository extends JpaRepository<GalleryFileEntity, Long> {

	List<GalleryFileEntity> findByGalleryEntity(GalleryEntity galleryEntity);

}
