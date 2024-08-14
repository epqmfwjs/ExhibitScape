package com.ExhibitScape.app.dto.scheduleBoard;

import java.time.LocalDateTime;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.domain.scheduleBoard.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDTO {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long C_no;
    private String content;
    private String memberId;


    @Builder
    public CommentResponseDTO(Comment comment) {
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.C_no = comment.getC_no();
        this.content = comment.getContent();
        this.memberId = comment.getMember().getMemberId();
    }
}

