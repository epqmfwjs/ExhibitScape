package com.ExhibitScape.app.controller.gallery;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ExhibitScape.app.domain.gallery.GalleryEntity;
import com.ExhibitScape.app.dto.gallery.GalCommentDTO;
import com.ExhibitScape.app.dto.gallery.GalleryDTO;
import com.ExhibitScape.app.service.gallery.GalCommentService;
import com.ExhibitScape.app.service.gallery.GalleryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gallery")
public class GalleryController {

	private final GalleryService galleryService;
	private final GalCommentService galCommentService;

	@GetMapping("/save")
	public String save() {
		return "gallery/save";
	}

	@PostMapping("/save")
	public String save(GalleryDTO galleryDTO) throws IllegalStateException, IOException {
		System.out.println("GalleryDTO = " + galleryDTO);
		galleryService.save(galleryDTO);
		return "gallery/index";
	}

	@GetMapping("/")
	public String findAll(Model model) {
		List<GalleryDTO> galleryDTOList = galleryService.findAll();
		model.addAttribute("galleryList", galleryDTOList);
		return "gallery/list";
	}

	@GetMapping("/{id}")
	public String findByID(@PathVariable("id") Long id, Model model) {
		galleryService.updateHits(id);
		GalleryDTO galleryDTO = galleryService.findById(id);
		
		/*댓글 목록 추가*/
        List<GalCommentDTO> commentList = galCommentService.findAll(id);
		/*댓글 추가 완료*/
		
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

//	@PostMapping("/update")
//	public String update(@ModelAttribute GalleryDTO galleryDTO, Model model) {
//		GalleryDTO gallery = galleryService.update(galleryDTO);
//		model.addAttribute("gallery", gallery);
//		// return "gallery/detail";
//		return "redirect:/gallery/" + gallery.getGalId();
//	}

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

    
	@ResponseBody
    @GetMapping("/gallery/searchByDateRange")
    public String searchGalleryByDateRange(Model model, 
                                            @RequestParam("startDate") String startDate, 
                                            @RequestParam("endDate") String endDate) {
        List<GalleryEntity> galleryList = galleryService.searchGalleryByDateRange(startDate, endDate);
        model.addAttribute("galleryList", galleryList);
        return "gallery :: galleryList";
    }
	

//	@GetMapping("/paging")
//	public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
////		pageable.getPageNumber();
//		Page<GalleryDTO> galleryList = galleryService.paging(pageable);
//		int blockLimit = 5;
//		int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;																											// ~~
//		int endPage = ((startPage + blockLimit - 1) < galleryList.getTotalPages()) ? startPage + blockLimit - 1
//				: galleryList.getTotalPages();
//
//		model.addAttribute("boardList", galleryList);
//		model.addAttribute("startPage", startPage);
//		model.addAttribute("endPage", endPage);
//		return "paging";
//	}
}
