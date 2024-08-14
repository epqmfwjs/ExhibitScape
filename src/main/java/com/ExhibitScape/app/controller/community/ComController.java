package com.ExhibitScape.app.controller.community;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ExhibitScape.app.domain.community.Community;
import com.ExhibitScape.app.domain.community.CommunityRepository;
import com.ExhibitScape.app.dto.community.CommunityDTO;
import com.ExhibitScape.app.service.community.CommunityService;



@Controller
@RequestMapping("/exhibitscape")
public class ComController {
	
	@Autowired
	private CommunityRepository communityRepository;
	
	@Autowired
	private CommunityService communityService;
	
	@Value("${spring.servlet.multipart.location}")//application.properties의 변수
	private String uploadPath;
	
	@GetMapping("/community/communityWriteView")
	public String communityWriteView() {
		return "/community/communityWriteView";
	}
	
	//글쓰기
	@PostMapping("/community/communityWrite")
	public String communityWrite(CommunityDTO communityDTO,@RequestParam("comFile") MultipartFile[] comFiles) {
		
		    String comImgSname = null;
		    String comImgPath =null;
	
		 // 파일 업로드 처리
	    for (MultipartFile comFile : comFiles) {
	        // 이미지 파일만 업로드 가능
	        if (!comFile.getContentType().startsWith("image")) {
	            continue;
	        }

	        // 실제 파일이름 IE나 Edge는 전체 경로가 들어옴
	        String comImgOname = comFile.getOriginalFilename();
	        String fileName = comImgOname.substring(comImgOname.lastIndexOf("\\") + 1);
	        
	        // 파일 확장자 추출
	        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

	        // 날짜 폴더 생성
	        String folderPath = communityService.makeFolder();

	        String uuid = UUID.randomUUID().toString();
	        comImgSname = uuid + fileExtension;
	        comImgPath =folderPath;
	        
	        try {
	            comFile.transferTo(Paths.get(uploadPath,comImgPath,comImgSname));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    communityDTO.setComImgPath(comImgPath);
	    communityDTO.setComImgSname(comImgSname);
		communityService.communityWrite(communityDTO);
		// CommunityDTO에 파일 정보 설정
		return "redirect:/exhibitscape/community/communityList";
	}
	
	//목록 조회
	@GetMapping("/community/communityList")
	public String communityList(Model model,@RequestParam(value="page", defaultValue="0") int page
								,@RequestParam(value = "category", required = false) String category){
		//@RequestParam(value="page", defaultValue="0") int page,
		//1.파라미터받기
		//2.비즈니스로직수행
		//기본 목록 조회
//		List<CommunityDTO> comDTOList = communityService.communityList();
//		model.addAttribute("comDTOList", comDTOList);
		
		//페이징처리 목록 조회
		Page<Community> pageCommunity;
	    Pageable pageable = PageRequest.of(page, 10);
	    
	    if (category == null || category.isEmpty() || category.equals("전체")) {
	        pageCommunity = communityService.findAllCommunities(pageable);
	    } else {
	        pageCommunity = communityService.findCommunitiesByCategory(category, pageable);
	    }
	    
	    model.addAttribute("pageCommunity", pageCommunity);
		return "/community/comListView";
	}
	
	
	//카테고리 post방식 
	@PostMapping("/community/communityList")
	public String communityListPost(Model model,
	                                @RequestParam(value = "page", defaultValue = "0") int page,
	                                @RequestParam(value = "category", required = false) String category) {
		Page<Community> pageCommunity;
	    Pageable pageable = PageRequest.of(page, 10);
	    
	    if (category == null || category.isEmpty() || category.equals("전체")) {
	        pageCommunity = communityService.findAllCommunities(pageable);
	    } else {
	        pageCommunity = communityService.findCommunitiesByCategory(category, pageable);
	    }
	    
	    model.addAttribute("pageCommunity", pageCommunity);
		return "/community/comListView";
	}
	
	//글 수정 페이지
	@GetMapping("/community/communityReWriteView/{comId}")
	public String communityReWrite(@PathVariable("comId") Integer comId,Model model) {
		//1.파라미터받기
		//2.비즈니스로직수행
		CommunityDTO comDTO = communityService.communityDetail(comId);
		model.addAttribute("comDTO", comDTO);
		//4.view
		return "/community/communityReWriteView";
	}
	
	@PostMapping("/community/communityReWriteView/{comId}")
	public String communityReWriteViewPost(@PathVariable("comId") Integer comId, Model model) {
	    CommunityDTO comDTO = communityService.communityDetail(comId);
	    model.addAttribute("comDTO", comDTO);
	    return "community/communityReWriteView";
	}
	
	//글 수정
	@PostMapping("/community/communityReWrite/{comId}")
	public String communityUpdate(@ModelAttribute CommunityDTO comDTO,
            @RequestParam("comFile") MultipartFile[] comFiles,
            @RequestParam("comImgSname") String comImgSname,
            @RequestParam("comImgPath") String comImgPath,
            @PathVariable("comId") Integer comId){
		
		comDTO.setComImgSname(comImgSname);
	    comDTO.setComImgPath(comImgPath);
	    System.out.println(comId);

	    // 파일 업로드 처리
	    if (comFiles != null && comFiles.length > 0 && !comFiles[0].isEmpty()) {
	        MultipartFile comFile = comFiles[0];

	        // 이미지 파일만 업로드 가능
	        if (comFile.getContentType().startsWith("image")) {
	            // 기존 파일 삭제
	            if (comImgSname != null && !comImgSname.isEmpty()) {
	                Path oldFilePath = Paths.get(uploadPath, comImgPath, comImgSname);
	                try {
	                    Files.deleteIfExists(oldFilePath);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            // 새로운 파일 업로드
	            String comImgOname = comFile.getOriginalFilename();
	            String fileName = comImgOname.substring(comImgOname.lastIndexOf("\\") + 1);
	            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
	            String folderPath = communityService.makeFolder();
	            String uuid = UUID.randomUUID().toString();
	            comImgSname = uuid + fileExtension;
	            comImgPath = folderPath;

	            try {
	                comFile.transferTo(Paths.get(uploadPath, comImgPath, comImgSname));
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            
	    	    comDTO.setComImgPath(comImgPath);
	    	    comDTO.setComImgSname(comImgSname);
	        }
	    }else {
	        // 새로운 파일을 업로드하지 않은 경우, 기존 파일명과 경로를 그대로 유지
	        comDTO.setComImgPath(comImgPath);
	        comDTO.setComImgSname(comImgSname);   

	    }
	       
	    comDTO.setComId(comId);
	    communityService.communityReWrite(comDTO);
	    return "redirect:/exhibitscape/community/communityDetail/" + comDTO.getComId();
	}
	
	
	//글 상세페이지
		@GetMapping("/community/communityDetail/{comId}")
		public String communityDetail(@PathVariable("comId") Integer comId,Model model){
			//1.파라미터받기
			//2.비즈니스로직수행
			CommunityDTO comDTO = communityService.communityDetail(comId);
			model.addAttribute("comDTO", comDTO);
			//System.out.println(comDTO.getCommentList().toString());
			//4.view
			return "/community/communityDetailView";
		}
	

}
