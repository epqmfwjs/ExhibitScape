package com.ExhibitScape.app.domain.scheduleBoard;

import com.ExhibitScape.app.domain.member.MemberDomain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long C_no;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "scheduleboard_no")
    private ScheduleBoardList scheduleBoardList;
    

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberDomain member;

    public void update(String content) {
        this.content = content;
    }
}