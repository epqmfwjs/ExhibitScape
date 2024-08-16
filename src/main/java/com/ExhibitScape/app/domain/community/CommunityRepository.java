package com.ExhibitScape.app.domain.community;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CommunityRepository extends JpaRepository<Community, Integer> {

	//목록조회
	List<Community> findAllByOrderByPostDateDesc();
	Page<Community> findAllByOrderByPostDateDesc(Pageable pageable);
	Page<Community> findByComCategoryContainingOrderByPostDateDesc(String category, Pageable pageable);

	//희경추가
	List<Community> findByMemberId(String memberId);
    
	@Query(value = "SELECT DATE_FORMAT(postDate, '%Y-%m-%d') AS date, COUNT(*) AS count " +
            "FROM Community " +
            "GROUP BY date " +
            "ORDER BY date ASC", nativeQuery = true)
    List<Object[]> getBoardCountsByDate();
}
