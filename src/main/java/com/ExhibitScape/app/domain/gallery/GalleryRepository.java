package com.ExhibitScape.app.domain.gallery;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


//    @Query("SELECT g FROM GalleryEntity g WHERE g.galPeriod BETWEEN :startDate AND :endDate")
//    List<GalleryEntity> findByGalPeriodBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

	//문자열로 저장된 galPeriod 필드를 파싱하여 날짜 범위 검색을 수행
	@Query("SELECT g FROM GalleryEntity g WHERE CONCAT(SUBSTRING(g.galPeriod, 1, 10), ' - ', SUBSTRING(g.galPeriod, 12, 10)) BETWEEN :startDateRange AND :endDateRange")
	List<GalleryEntity> findByGalPeriodBetween(@Param("startDateRange") String startDateRange, @Param("endDateRange") String endDateRange);
	
	Page<GalleryEntity> findByGalTitleContaining(String searchWord, Pageable pageable);
	Page<GalleryEntity> findByGalLocationContaining(String searchWord, Pageable pageable);
	Page<GalleryEntity> findByGalAddressContaining(String searchWord, Pageable pageable);	

//    @Modifying
//    @Query("update GalleryEntity g set g.rec = g.rec + 1 where g.galId = :galId and :memberId not in (select m.id from g.rec m)")
//    int addRecommendation(@Param("galId") Long galId, @Param("memberId") String memberId);

//    @Modifying
//    @Query("update GalleryEntity g set g.rec = g.rec - 1 where g.galId = :galId and :memberId in (select m.id from g.rec m)")
//    int removeRecommendation(@Param("galId") Long galId, @Param("memberId") String memberId);
//
//    @Query("select count(g.rec) from GalleryEntity g where g.galId = :galId")
//    int getRecommendationCount(@Param("galId") Long galId);
//    
    //Page<GalleryEntity> findAll(Pageable pageable);
    

}	

