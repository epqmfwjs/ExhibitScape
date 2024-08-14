package com.ExhibitScape.app.domain.gallery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryEntity, Long> {

	@Modifying
	@Query(value="update GalleryEntity g set g.galHits = g.galHits+1 where g.galId=:id")
	void updateHits(@Param("id") Long id);


	List<GalleryEntity> findByGalLocationContaining(String searchWord);


	List<GalleryEntity> findByGalTitleContaining(String searchWord);


	List<GalleryEntity> findByGalAddressContaining(String searchWord);


	List<GalleryEntity> findByGalCategoryContaining(String category);


    @Query("SELECT g FROM GalleryEntity g WHERE g.galPeriod BETWEEN :startDate AND :endDate")
    List<GalleryEntity> findByGalPeriodBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);
}	

