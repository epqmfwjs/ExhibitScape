package com.ExhibitScape.app.service.scheduleBoard;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.domain.scheduleBoard.ScheduleBoardList;
import com.ExhibitScape.app.domain.scheduleBoard.ScheduleBoardRepository;
import com.ExhibitScape.app.domain.scheduleBoard.SearchKeyword;
import com.ExhibitScape.app.domain.scheduleBoard.SearchKeywordRepository;
import com.ExhibitScape.app.dto.scheduleBoard.BoardVO;



@Service
public class ScheduleBoardService {

	private ScheduleBoardRepository boardRepository;
	
	@Autowired
    public ScheduleBoardService(ScheduleBoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }
	@Autowired
	private SearchKeywordRepository searchKeywordRepository;
	
	//검색 (리스트상태에서의 검색)
	public Page<ScheduleBoardList> searchByTitle(Specification<ScheduleBoardList> spec, Pageable pageable) {
		return boardRepository.findAll(spec, pageable);
	}
	//인기검색어로직 1
	public void updateSearchCount(String keyword) {
	    SearchKeyword searchKeyword = searchKeywordRepository.findByKeyword(keyword)
	            .orElse(new SearchKeyword(keyword, 0L));
	    searchKeyword.incrementCount();
	    searchKeywordRepository.save(searchKeyword); 
	}
	//인기검색어로직 2
	public List<String> getPopularSearchKeywords(int limit) {
	    return boardRepository.findTopKeywords(PageRequest.of(0, limit));
	}
	
	//검색 (캘린더상태에서의 검색)
	public List<ScheduleBoardList> findAll(Specification<ScheduleBoardList> spec) {
		return boardRepository.findAll(spec);
	}
	
	//캘린더 전체 조회
	 public List<ScheduleBoardList> calendarList() {
		return boardRepository.findAll((Sort.by(Sort.Direction.DESC, "no")));
	    }
	 
	 //전체조회 페이징포함
	 public Page<ScheduleBoardList> pageList(Pageable pageable) {
		 return boardRepository.findAll(pageable);
	 }
	 
	  //캘린더모드 행사일정 범위별 검색
	  public List<ScheduleBoardList> findAllBystart_dateBetween(LocalDate start_date, LocalDate end_date) {
	      return boardRepository.findAllBystart_dateBetween(start_date, end_date);
	  }
	  
	  //리스트모드 행사일정 범위별 검색
	  public Page<ScheduleBoardList> findAllBystart_dateBetween(LocalDate start_date, LocalDate end_date,Pageable pageable) {
	      return boardRepository.findAllBystart_dateBetween(start_date, end_date, pageable);
	  }
	  
	 
	  //등록
	  public void insert(BoardVO boardVO) {
		 ScheduleBoardList boardlist = new ScheduleBoardList(boardVO.getTitle(),
				 boardVO.getMaster(),boardVO.getMaster_TEL(),boardVO.getMaster_EMAIL(),boardVO.getContent(),
				 boardVO.getStart_date(),boardVO.getEnd_date(),boardVO.getPrice(),
				 boardVO.getPlace(),boardVO.getOrganizers(),boardVO.getImgOFile());
	      boardRepository.save(boardlist);
	  }
	 
	  //상세조회
	  public ScheduleBoardList detail(Long no) {
		  return boardRepository.findById(no).orElse(null);
	  }
	  
	  //삭제
	  public void delete(Long no) {
		  boardRepository.deleteById(no);
	  }
	  
	  //수정
	  public void update(BoardVO boardVO) {
		 ScheduleBoardList boardlist = new ScheduleBoardList(boardVO.getNo(),boardVO.getTitle(),
				 boardVO.getMaster(),boardVO.getMaster_TEL(),boardVO.getMaster_EMAIL(),boardVO.getContent(),
				 boardVO.getStart_date(),boardVO.getEnd_date(),boardVO.getPrice(),
				 boardVO.getPlace(),boardVO.getOrganizers(),boardVO.getImgOFile());
	      boardRepository.save(boardlist);
	  }
}
