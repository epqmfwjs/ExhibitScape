package com.ExhibitScape.app.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ExhibitScape.app.domain.community.Community;
import com.ExhibitScape.app.dto.admin.MemberDTO;
import com.ExhibitScape.app.dto.community.CommunityDTO;
import com.ExhibitScape.app.service.admin.MemberService;
import com.ExhibitScape.app.service.community.CommunityService;
import com.ExhibitScape.app.service.gallery.GalleryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
	
    private final MemberService memberService;
    private final CommunityService communityService;
    private final GalleryService galleryService;

    
    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/list")
	public String findAll(Model model) {
		List<MemberDTO> memberDTOList = memberService.findAll();
		model.addAttribute("memberList", memberDTOList);
		return "/admin/list";
	}
	
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/update/{memberNo}")
    public String updateForm(@PathVariable("memberNo") Integer memberNo, Model model) {
        MemberDTO memberDTO = memberService.findById(memberNo);
        model.addAttribute("updateMember", memberDTO);
        return "/admin/update";
    }
	
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute MemberDTO memberDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/update";
        }
        memberService.update(memberDTO);
        return "redirect:/admin/list"; // 업데이트 후 리스트 페이지로 이동하도록 수정
    }
   
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{memberNo}")
    public String deleteById(@PathVariable("memberNo") Integer memberNo) {
        memberService.deleteById(memberNo);
        return "redirect:/admin/list";
    }

    
    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        int communityCount = communityService.getTotalCount();
        int galleryCount = galleryService.getTotalCount();

        Map<String, Integer> boardCounts = new HashMap<>();
        boardCounts.put("Community", communityCount);
        boardCounts.put("Gallery", galleryCount);

        Map<String, Integer> communityCounts = communityService.getBoardCountsByDate();

        model.addAttribute("boardCounts", boardCounts);
        model.addAttribute("communityCounts", communityCounts);
        return "admin/dashboard";
    }
    
    @ResponseBody
    @PostMapping("/getUserCommunity")
    public List<CommunityDTO> getUserCommunity(@RequestParam("memberId") String memberId) {
        List<Community> communities = communityService.getUserCommunity(memberId);
        List<CommunityDTO> userBoardList = new ArrayList<>();

        for (Community community : communities) {
            CommunityDTO dto = new CommunityDTO();
            dto.setComId(community.getComId());
            dto.setMemberId(community.getMemberId());
            dto.setComTitle(community.getComTitle());
            dto.setComContent(community.getComContent());
            dto.setPostDate(community.getPostDate());
            dto.setComImgSname(community.getComImgSname());
            dto.setComImgPath(community.getComImgPath());
            dto.setComCategory(community.getComCategory());
            dto.setComHits(community.getComHits());
            dto.setComTag(community.getComTag());

            userBoardList.add(dto);
        }

        return userBoardList;
    }
    
//    @ResponseBody
//    @PostMapping("/getUserCommunity")
//    public List<Map<String, Object>> getUserCommunity(@RequestParam("memberId") String memberId) {
//        List<Community> communities = communityService.getUserCommunity(memberId);
//        List<Map<String, Object>> userBoardList = new ArrayList<>();
//
//        for (Community community : communities) {
//            Map<String, Object> data = new HashMap<>();
//            data.put("comId", community.getComId());
//            data.put("memberId", community.getMemberId());
//            data.put("comTitle", community.getComTitle());
//            data.put("postDate", community.getPostDate());
//
//            userBoardList.add(data);
//        }
//
//        return userBoardList;
//    }
    
}
