package com.ExhibitScape.app.domain.community;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommunityRepository extends JpaRepository<Community, Integer> {

	//목록조회
	List<Community> findAllByOrderByPostDateDesc();
	Page<Community> findAllByOrderByPostDateDesc(Pageable pageable);
	Page<Community> findByComCategoryContainingOrderByPostDateDesc(String category, Pageable pageable);

	
}
