package com.ExhibitScape.app.domain.scheduleBoard;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ScheduleBoardRepository extends JpaRepository<ScheduleBoardList, Long>, JpaSpecificationExecutor<ScheduleBoardList> {
	
	
	//리스트모드 타이틀 라이크검색 페이징처리
	Page<ScheduleBoardList> findAll(Specification<ScheduleBoardList> spec, Pageable pageable);
	
	//캘린더모드 타이틀 라이크검색 
	List<ScheduleBoardList> findAll(Specification<ScheduleBoardList> spec);
	
	//리스트모드 전체조회
	//create_date 를 기준으로 정렬하고싶지만 컨트롤러에서 sort 정의를 할떄 인식하지 못하는 상황이 발생하여 명시적으로 지정해줌.
	@Query(value = "SELECT s FROM ScheduleBoardList s ORDER BY s.create_date DESC")
	Page<ScheduleBoardList> findAll(Pageable pageable);
	
	//캘린더모드 행사일정범위검색
	@Query("SELECT s FROM ScheduleBoardList s WHERE s.start_date BETWEEN :start_date AND :end_date")
	List<ScheduleBoardList> findAllBystart_dateBetween(@Param("start_date") LocalDate start_date, @Param("end_date") LocalDate end_date);
	
	//리스트모드 행사일정범위검색
	@Query("SELECT s FROM ScheduleBoardList s WHERE s.start_date BETWEEN :start_date AND :end_date")
	Page<ScheduleBoardList> findAllBystart_dateBetween(@Param("start_date") LocalDate start_date, @Param("end_date") LocalDate end_date, Pageable pageable);

    @Query("SELECT k.keyword FROM SearchKeyword k ORDER BY k.count DESC")
    List<String> findTopKeywords(Pageable pageable);

}
