package com.ExhibitScape.app.service.scheduleBoard;

import java.util.List;

import com.ExhibitScape.app.dto.scheduleBoard.CommentRequestDTO;
import com.ExhibitScape.app.dto.scheduleBoard.CommentResponseDTO;



public interface CommentService {

    //댓글 작성 메소드
    Long writeComment(CommentRequestDTO commentRequestDTO, Long no,String memberId);

    //댓글 조회 메소드
    List<CommentResponseDTO> commentList(Long no);

    //댓글 수정 메소드
    void updateComment(CommentRequestDTO commentRequestDTO, Long C_no,String memberId);

    //댓글 삭제 메소드

	void deleteComment(Long commentId, String memberId);
}