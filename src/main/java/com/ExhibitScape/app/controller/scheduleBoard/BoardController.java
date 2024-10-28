package com.ExhibitScape.app.controller.scheduleBoard;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ExhibitScape.app.domain.scheduleBoard.ScheduleBoardList;
import com.ExhibitScape.app.domain.scheduleBoard.ScheduleBoardListSpecification;
import com.ExhibitScape.app.dto.scheduleBoard.BoardVO;
import com.ExhibitScape.app.dto.scheduleBoard.CommentResponseDTO;
import com.ExhibitScape.app.service.scheduleBoard.CommentService;
import com.ExhibitScape.app.service.scheduleBoard.ScheduleBoardService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Controller
@RequestMapping("/scheduleBoard")
public class BoardController {

	@Autowired
	private ScheduleBoardService boardService;
	private final CommentService commentService;
	
	//리스트모드 키워드검색 페이징처리된 검색리스트조회	
	@GetMapping("/search")
	public String searchSchedule(Model model,RedirectAttributes redirectModel, 
	                             @RequestParam(value = "listKeyword", required = false) String listKeyword,
	                             @RequestParam(value = "page", defaultValue = "0") int page,
	                             @RequestParam(value = "size", defaultValue = "8") int size) {
	    // 검색 횟수 업데이트 로직
	    boardService.updateSearchCount(listKeyword);
	    Pageable pageable = PageRequest.of(page, size);
	    Specification<ScheduleBoardList> spec = ScheduleBoardListSpecification.titleOrContentContains(listKeyword);
	    System.out.println("요청키워드 "+listKeyword);
	    Page<ScheduleBoardList> searchList = boardService.searchByTitle(spec, pageable);
	    int totalPage = searchList.getTotalPages();
	    String url = null;
	   if(searchList.isEmpty()) {
		   redirectModel.addFlashAttribute("msg","/image/scheduleBoard/noResult.jpg");
	        url = "redirect:/scheduleBoard/list";
	   }else {
	    model.addAttribute("DBlist", searchList.getContent());
	    model.addAttribute("totalPage", totalPage); // 총 페이지 수를 모델에 추가
	    model.addAttribute("currentPage", page); // 현재 페이지 번호를 모델에 추가
	    model.addAttribute("size", size); // 페이지 사이즈를 모델에 추가
	    url = "scheduleBoard/list";

	   }  
	    return url; 
	}	    
    
	//리스트모드 날짜범위검색 페이징처리된 검색리스트조회	
    @GetMapping("/searchDate")
    public String searchScheduleDate(Model model,RedirectAttributes redirectModel, 
						    		@RequestParam(value = "listStart_date", required = false) LocalDate listStart_date,
						    		@RequestParam(value = "listEnd_date", required = false) LocalDate listEnd_date,
						    		@RequestParam(value = "page", defaultValue = "0") int page,
						    		@RequestParam(value = "size", defaultValue = "8") int size) {
    	Pageable pageable = PageRequest.of(page, size);
    	Page<ScheduleBoardList> searchList = boardService.findAllBystart_dateBetween(listStart_date, listEnd_date,pageable);
    	int totalPage = searchList.getTotalPages();
    	String url = null;
    	if(searchList.isEmpty()) {
    		redirectModel.addFlashAttribute("msg","/image/scheduleBoard/noResult.jpg");
    		url = "redirect:/scheduleBoard/list";
    	}else {
    		model.addAttribute("DBlist", searchList.getContent());
    		model.addAttribute("totalPage", totalPage); // 총 페이지 수를 모델에 추가
    		model.addAttribute("currentPage", page); // 현재 페이지 번호를 모델에 추가
    		model.addAttribute("size", size); // 페이지 사이즈를 모델에 추가
    		url = "scheduleBoard/list";
    	}  
    	return url; 
	}

	@GetMapping("/")
	public String index (){

		return "index";
	}

	//리스트모드 전체 리스트 조회 페이징처리
	@GetMapping("/list")
	public String list(Model model, 
	                   @RequestParam(required = false, defaultValue = "0", value = "page") int page,
	                   @RequestParam(required = false, defaultValue = "8", value = "size") int size) {
	    
	    Sort sort = Sort.by(Sort.Direction.DESC, "create_date");
	    // 페이지 요청 정보와 정렬 조건을 함께 생성
	    Pageable pageable = PageRequest.of(page, size, sort);
	    Page<ScheduleBoardList> DBlist = boardService.pageList(pageable); // pageable을 사용하여 서비스에 전달
	    int totalPage = DBlist.getTotalPages(); // 총 페이지 수 계산
	    
	    model.addAttribute("DBlist", DBlist.getContent()); // 조회된 목록을 모델에 추가
	    model.addAttribute("totalPage", totalPage); // 총 페이지 수를 모델에 추가
	    model.addAttribute("currentPage", page); // 현재 페이지 번호를 모델에 추가
	    model.addAttribute("size", size); // 페이지 사이즈를 모델에 추가
	    
	    return "scheduleBoard/list";
	}

	//글등록폼요청
	@GetMapping("/insert")
	public String insert() {
		return "scheduleBoard/insertFrm";
	}
	
	//글등록
    @PostMapping("/insert")
	public String insert(BoardVO boardVO) {
		boardService.insert(boardVO);
		return "redirect:/scheduleBoard/list";
	}
	
    //글상세조회
    @RequestMapping("/detail/{no}")
    public String detail(@PathVariable("no") Long no, Model model, Authentication authentication) {

    	String memberId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            memberId = userDetails.getUsername();
            System.out.println(memberId);
        }
        
        ScheduleBoardList boardlist = boardService.detail(no);
        List<CommentResponseDTO> commentResponseDTO = commentService.commentList(no);
        
        model.addAttribute("comments", commentResponseDTO);
        model.addAttribute("detailList", boardlist);
        model.addAttribute("no", no);
        
        if (memberId != null) {
            model.addAttribute("memberId", memberId);
        }
        return "scheduleBoard/detail";
    }

	//글삭제
	@GetMapping("/delete")
	public String delete(@RequestParam("no") Long no,Model model) {
		boardService.delete(no);
		return"redirect:/scheduleBoard/list";
	}
	
	//글수정 폼 요청
	@GetMapping("/update")
	public String update(@RequestParam("no") Long no,Model model) {
		model.addAttribute("no", no);
		return"scheduleBoard/updateFrm";
	}
	
	//글수정
	@PostMapping("/update")
	public String updateRE(@RequestParam("no") Long no,BoardVO boardVO) {
		boardVO.setNo(no);
		boardService.update(boardVO);
		return"redirect:/scheduleBoard/detail"+"?no="+no;
	}
	
	//로직구현을위한 테스트 뷰 요청
	@GetMapping("/test")
	public String test() {
		return"/scheduleBoard/test";
	}
}
