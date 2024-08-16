package com.ExhibitScape.app.controller.community;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.ExhibitScape.app.domain.community.Community;
import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.dto.community.CommunityDTO;
import com.ExhibitScape.app.service.community.CommunityService;
import com.ExhibitScape.app.service.member.MemberDetailsService;



@Controller
@RequestMapping("/exhibitscape")
public class ComController {
	
	@Autowired
	private CommunityService communityService;
	
	@Autowired
	private MemberDetailsService memberDetailsService;
	
	@Value("${spring.servlet.multipart.location}")//application.properties의 변수
	private String uploadPath;
	
	//좋아요!
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/community/{comId}/like")
	@ResponseBody
	public Map<String, Object> toggleComLike(@PathVariable("comId") Integer comId, Principal principal) {
	    MemberDomain memberDomain = memberDetailsService.getUser(principal.getName());
	    boolean liked = communityService.toggleComLike(comId, memberDomain);
	    int likeCount = communityService.getLikeCount(comId);
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("liked", liked);
	    response.put("likeCount", likeCount);
	    
	    return response;
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/community/{comId}")
	public String getCommunityDetails(@PathVariable("comId") Integer comId, Model model, Principal principal) {
	    MemberDomain memberDomain = memberDetailsService.getUser(principal.getName());
	    CommunityDTO communityDTO = communityService.getCommunityDetails(comId, memberDomain);
	    
	    model.addAttribute("communityDTO", communityDTO);
	    return "community/details";
	}
	
	
	//글쓰기 폼
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/community/communityWriteView")
	public String communityWriteView(Principal principal,RedirectAttributes redirectAttributes ) {
		
		if (principal == null || principal.getName().isEmpty()) {
	        redirectAttributes.addFlashAttribute("errorMessage", "로그인을 하셔야 글쓰기를 할 수 있습니다.");
	        return "redirect:/exhibitscape/community/communityList"; // 로그인 페이지로 리다이렉트
	    }
		return "/community/communityWriteView";
	}
	
	//글쓰기
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/community/communityWrite")
	public String communityWrite(CommunityDTO communityDTO,@RequestParam("comFile") MultipartFile[] comFiles,
									Principal principal
									,@RequestParam(value="placeLat", required = false) String placeLat
									,@RequestParam(value="placeLong", required = false) String placeLong
									,@RequestParam(value="placeName", required = false) String placeName) {
			
			//로그인 유저의 정보를 가져오기
			String memberId = principal.getName();
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
	    
	    //지도 처리
	     if(placeLat!=null && placeLong !=null && placeName!=null) {
	    	 communityDTO.setPlaceLat(placeLat);
	    	 communityDTO.setPlaceLong(placeLong);
	    	 communityDTO.setPlaceName(placeName);
	     }
	     
	    communityDTO.setMemberId(memberId);
	    communityDTO.setComImgPath(comImgPath);
	    communityDTO.setComImgSname(comImgSname);
		communityService.communityWrite(communityDTO);
		// CommunityDTO에 파일 정보 설정
		return "redirect:/exhibitscape/community/communityList";
	}
	
	@GetMapping("/map-data")
	@ResponseBody
	public Map<String, Object> getMapData(@RequestParam("comId") Integer comId) {
	    System.out.println("getMapData///////////////////////////////////////////////");
	    CommunityDTO comDTO = communityService.communityDetailWithoutLike(comId);

	    Map<String, Object> data = new HashMap<>();
	    data.put("placeLat", comDTO.getPlaceLat());
	    data.put("placeLong", comDTO.getPlaceLong());
	    data.put("placeName", comDTO.getPlaceName());
	    return data;
	}
	
	//목록 조회
	@GetMapping("/community/communityList")
	public String communityList(Model model,@RequestParam(value="page", defaultValue="0") int page
								,@RequestParam(value = "category", required = false) String category,Principal principal){
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
	    
	    // 좋아요 정보 조회
	    List<Integer> likedCommunity = null;
	    
	    if (principal != null) {
	        String memberId = principal.getName();
	        likedCommunity = communityService.findLikedCommunityIds(memberId);
	    }
	    
	    model.addAttribute("likedCommunity", likedCommunity);
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
	
	@PreAuthorize("isAuthenticated()")
	//글 수정 페이지
	@GetMapping("/community/communityReWriteView/{comId}")
	public String communityReWrite(@PathVariable("comId") Integer comId,Model model,Principal principal) {
		//1.파라미터받기
		//2.비즈니스로직수행
		CommunityDTO comDTO = communityService.communityDetailWithoutLike(comId);
		
		if(!comDTO.getMemberId().equals(principal.getName())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
		}
		model.addAttribute("comDTO", comDTO);
		//4.view
		return "/community/communityReWriteView";
	}
	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/community/communityReWriteView/{comId}")
	public String communityReWriteViewPost(@PathVariable("comId") Integer comId, Model model,Principal principal) {
	    
		CommunityDTO comDTO = communityService.communityDetailWithoutLike(comId);
		
		if(!comDTO.getMemberId().equals(principal.getName())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
		}
		model.addAttribute("comDTO", comDTO);
		//4.view
		return "/community/communityReWriteView";
	}
	
	//글 수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/community/communityReWrite/{comId}")
	public String communityUpdate(@ModelAttribute CommunityDTO comDTO,
            @RequestParam("comFile") MultipartFile[] comFiles,
            @RequestParam("comImgSname") String comImgSname,
            @RequestParam("comImgPath") String comImgPath,
            @RequestParam("placeLat") String placeLat,
            @RequestParam("placeLong") String placeLong,
            @RequestParam("placeName") String placeName,
            @PathVariable("comId") Integer comId,
            Principal principal){
		 System.out.println(placeLat+placeLong);
		if(!comDTO.getMemberId().equals(principal.getName())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
		}
		comDTO.setPlaceLat(placeLat);   
		comDTO.setPlaceLong(placeLong);  
		comDTO.setPlaceName(placeName);   
		comDTO.setComImgSname(comImgSname);
	    comDTO.setComImgPath(comImgPath);
	    
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
	public String communityDetail(@PathVariable("comId") Integer comId, Model model, Principal principal) {
	    // 1. 파라미터 받기
	    // 2. 비즈니스 로직 수행
	    CommunityDTO comDTO;
	    if (principal != null) {
	        String memberId = principal.getName();
	        MemberDomain memberDomain = memberDetailsService.getUser(memberId);
	        comDTO = communityService.communityDetailWithLike(comId, memberDomain);
	    } else {
	        comDTO = communityService.communityDetailWithoutLike(comId);
	    }
	    
	    System.out.println(comDTO);
	    model.addAttribute("comDTO", comDTO);
	    
	    // 4. view
	    return "/community/communityDetailView";
	}
		
	
	//삭제처리
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{comId}")
	public String communityDelete(@PathVariable("comId") Integer comId,Principal principal) {
		
		String memberId = principal.getName();
		MemberDomain memberDomain = memberDetailsService.getUser(memberId);
		CommunityDTO comDTO = communityService.getCommunityDetails(comId, memberDomain);
		
		if(!comDTO.getMemberId().equals(principal.getName())){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");
		}
		
		communityService.delete(comDTO);
		
		return "redirect:/exhibitscape/community/communityList";
	}
	
}
