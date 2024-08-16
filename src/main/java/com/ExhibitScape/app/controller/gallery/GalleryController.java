package com.ExhibitScape.app.controller.gallery;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ExhibitScape.app.domain.gallery.GalleryEntity;
import com.ExhibitScape.app.dto.gallery.GalCommentDTO;
import com.ExhibitScape.app.dto.gallery.GalleryDTO;
import com.ExhibitScape.app.service.admin.MemberService;
import com.ExhibitScape.app.service.gallery.GalCommentService;
import com.ExhibitScape.app.service.gallery.GalleryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gallery")
public class GalleryController {

	private final GalleryService galleryService;
	private final GalCommentService galCommentService;
	private final MemberService memberService;

	@GetMapping("/save")
	public String save() {
		return "gallery/save";
	}

	@PostMapping("/save")
	public String save(GalleryDTO galleryDTO) throws IllegalStateException, IOException {
		System.out.println("GalleryDTO = " + galleryDTO);
		galleryService.save(galleryDTO);
		return "redirect:/gallery/";
	}
	
//	@GetMapping("/")
//	public String findAll(@RequestParam(value = "page", defaultValue = "1") int page,
//	                      @RequestParam(value = "searchOption", required = false) String searchOption,
//	                      @RequestParam(value = "searchWord", required = false) String searchWord,
//	                      Model model) {
//	    Pageable pageable = PageRequest.of(page - 1, 8, Sort.by("galId").descending());
//	    Page<GalleryDTO> galleryPage;
//
//	    if (searchOption != null && searchWord != null) {
//	        // 검색 기능 수행
//	        galleryPage = galleryService.search(searchOption, searchWord, pageable);
//	    } else {
//	        // 전체 갤러리 목록
//	        galleryPage = galleryService.findAll(pageable);
//	    }
//
//	    model.addAttribute("galleryList", galleryPage.getContent());
//	    model.addAttribute("currentPage", page);
//	    model.addAttribute("totalPages", galleryPage.getTotalPages());
//	    model.addAttribute("startPage", Math.max(1, page - 2));
//	    model.addAttribute("endPage", Math.min(galleryPage.getTotalPages(), page + 2));
//
//	    return "gallery/list";
//	}
	

	@GetMapping("/")
	public String findAll(@RequestParam(value = "page", defaultValue = "1") int page,
	                      @RequestParam(value = "searchOption", required = false) String searchOption,
	                      @RequestParam(value = "searchWord", required = false) String searchWord,
	                      Model model) {
	    Pageable pageable = PageRequest.of(page - 1, 8, Sort.by("galId").descending());
	    Page<GalleryDTO> galleryPage;

	    if (searchOption != null && searchWord != null) {
	        // 검색 기능 수행
	        galleryPage = galleryService.search(searchOption, searchWord, pageable);
	    } else {
	        // 전체 갤러리 목록
	        galleryPage = galleryService.findAll(pageable);
	    }

	    model.addAttribute("galleryList", galleryPage.getContent());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", galleryPage.getTotalPages());
	    model.addAttribute("startPage", Math.max(1, page - 2));
	    model.addAttribute("endPage", Math.min(galleryPage.getTotalPages(), page + 2));
	    model.addAttribute("searchOption", searchOption);
	    model.addAttribute("searchWord", searchWord);

	    return "gallery/list";
	}
	
	@GetMapping("/{id}")
	public String findByID(@PathVariable("id") Long id, Model model, Authentication authentication) {
	    galleryService.updateHits(id);
	    GalleryDTO galleryDTO = galleryService.findById(id);
	    
	    // 개행 문자를 <br> 태그로 변환
	    galleryDTO.setGalInfo(galleryDTO.getGalInfo().replace("\n", "<br/>"));
	    
	    /*댓글 목록 추가*/
	    List<GalCommentDTO> commentList = galCommentService.findAll(id);
	    /*댓글 추가 완료*/
	    
	    // 로그인한 사용자 정보 추가
	    if (authentication != null && authentication.isAuthenticated()) {
	        String memberId = ((UserDetails) authentication.getPrincipal()).getUsername();
	        //galleryDTO.setMemberId(memberId); // memberId 설정 ->서비스로 이동
	        model.addAttribute("memberId", memberId);
	        model.addAttribute("isAuthor", memberId.equals(galleryDTO.getMemberId()));
	    }else {
	        model.addAttribute("isAuthor", false);
	    }
	    
	    model.addAttribute("gallery", galleryDTO);
	    model.addAttribute("commentList", commentList);

	    return "gallery/detail";
	}
	
	@GetMapping("/update/{id}")
	public String updateForm(@PathVariable("id") Long id, Model model) {
		GalleryDTO galleryDTO = galleryService.findById(id);
		model.addAttribute("galleryUpdate", galleryDTO);
		return "gallery/update";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute GalleryDTO galleryDTO, Model model) throws IOException {
		galleryService.update(galleryDTO);
		return "redirect:/gallery/" + galleryDTO.getGalId();
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id) {
		galleryService.delete(id);
		return "redirect:/gallery/";
	}

	@GetMapping("/search")
	public String search(@RequestParam(name = "searchOption", required = false) String searchOption,
			@RequestParam(name = "searchWord", required = false) String searchWord, Model model) {
		// 검색 옵션과 검색어가 없는 경우
		if (searchOption == null || searchWord == null) {
			return "redirect:/gallery";
		}

		// 검색 기능 수행
		List<GalleryDTO> searchResult = galleryService.search(searchOption, searchWord);
		model.addAttribute("galleryList", searchResult);

		return "gallery/list";
	}
	
	
	@GetMapping("/searchByDateRange")
	public String searchByDateRange(@RequestParam("startDateRange") String startDateRange,
	                                @RequestParam("endDateRange") String endDateRange,
	                                Model model) {
	    List<GalleryDTO> galleryList = galleryService.searchByDateRange(startDateRange, endDateRange);
	    model.addAttribute("galleryList", galleryList);
	    return "gallery/list :: #galleryContainer"; // 갤러리 목록 부분만 렌더링
	}
	
//	@GetMapping("/searchByDateRange")
//	public String searchByDateRange(@RequestParam(value = "page", defaultValue = "1") int page,
//	                                @RequestParam("startDateRange") String startDateRange,
//	                                @RequestParam("endDateRange") String endDateRange,
//	                                Model model) {
//	    Pageable pageable = PageRequest.of(page - 1, 8, Sort.by("galId").descending());
//	    Page<GalleryDTO> galleryPage = galleryService.searchByDateRange(startDateRange, endDateRange, pageable);
//
//	    model.addAttribute("galleryList", galleryPage.getContent());
//	    model.addAttribute("currentPage", page);
//	    model.addAttribute("totalPages", galleryPage.getTotalPages());
//	    model.addAttribute("startPage", Math.max(1, page - 2));
//	    model.addAttribute("endPage", Math.min(galleryPage.getTotalPages(), page + 2));
//	    model.addAttribute("searchOption", null);
//	    model.addAttribute("searchWord", null);
//
//	    return "gallery/list";
//	}
	
}
