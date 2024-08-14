package com.ExhibitScape.app.service.scheduleBoard;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.domain.member.UserRepository;
import com.ExhibitScape.app.domain.scheduleBoard.Comment;
import com.ExhibitScape.app.domain.scheduleBoard.CommentRepository;
import com.ExhibitScape.app.domain.scheduleBoard.ScheduleBoardList;
import com.ExhibitScape.app.domain.scheduleBoard.ScheduleBoardRepository;
import com.ExhibitScape.app.dto.scheduleBoard.CommentRequestDTO;
import com.ExhibitScape.app.dto.scheduleBoard.CommentResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final UserRepository userRepository;
    private final ScheduleBoardRepository boardRepository;
    private final CommentRepository commentRepository;
    
    //댓글 작성 구현메소드
    @Override
    public Long writeComment(CommentRequestDTO commentRequestDTO, Long no,String memberId) {
    	MemberDomain member = userRepository.findByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("아이디가 존재하지않습니다."));
        ScheduleBoardList scheduleBoardList = boardRepository.findById(no).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        Comment result = Comment.builder()
                .content(commentRequestDTO.getContent())
                .scheduleBoardList(scheduleBoardList)
                .member(member)
                .build();
        commentRepository.save(result);
        return result.getC_no();
    }
    
    //댓글 조회  구현메소드
    @Override
    public List<CommentResponseDTO> commentList(Long id) {
    	ScheduleBoardList boardList = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        List<Comment> comments = commentRepository.findByScheduleBoardList(boardList);
        return comments.stream()
                .map(comment -> CommentResponseDTO.builder()
                .comment(comment)
                .build())
                .collect(Collectors.toList());
    	   	
    }
    
    //댓글 수정 구현메소드
    @Override
    public void updateComment(CommentRequestDTO commentRequestDTO, Long commentId,String memberId) {
    	Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        
        if (!comment.getMember().getMemberId().equals(memberId)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }
    	comment.update(commentRequestDTO.getContent());
        commentRepository.save(comment);
    }
    
    //댓글 삭제 구현메소드
    @Override
    public void deleteComment(Long commentId,String memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
            
        if (!comment.getMember().getMemberId().equals(memberId)) {
                throw new AccessDeniedException("삭제 권한이 없습니다.");
            }
            commentRepository.deleteById(commentId);
    }
}