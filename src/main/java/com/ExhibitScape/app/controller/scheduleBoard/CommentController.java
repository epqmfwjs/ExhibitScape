package com.ExhibitScape.app.controller.scheduleBoard;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ExhibitScape.app.dto.scheduleBoard.CommentRequestDTO;
import com.ExhibitScape.app.service.scheduleBoard.CommentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class CommentController {

    private final CommentService commentService;
    
     // 댓글 작성
    @PostMapping("/board/{no}/comment")
    public String writeComment(@PathVariable("no") String Sno, CommentRequestDTO commentRequestDTO, @RequestParam("no") Long no,
            Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요한 기능입니다.");
            return "redirect:/login";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberId = userDetails.getUsername();
        System.out.println("memberId : " + memberId);

        commentService.writeComment(commentRequestDTO, no, memberId);
        return "redirect:/scheduleBoard/detail/" + no;
    }

    // 댓글 수정
    @ResponseBody
    @PostMapping("/board/{no}/comment/{commentC_no}/update")
    public String updateComment(@PathVariable("no") Long no, @PathVariable("commentC_no") Long commentC_no, @RequestBody CommentRequestDTO commentRequestDTO,Authentication authentication) {
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    	String memberId = userDetails.getUsername();
    	System.out.println("수정요청 댓글내용 : "+commentRequestDTO.getContent());
    	commentService.updateComment(commentRequestDTO, commentC_no, memberId);
    	return "redirect:/scheduleBoard/detail/" + no;
    }

     // 댓글 삭제
    @GetMapping("/board/{no}/comment/{C_no}/remove")
    public String deleteComment(@PathVariable("no") Long no, @PathVariable("C_no") Long C_no,Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberId = userDetails.getUsername();
    	commentService.deleteComment(C_no,memberId);
        return "redirect:/scheduleBoard/detail/" + no;
    }
}