package com.ExhibitScape.app.domain.scheduleBoard;



import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Table(name = "scheduleboard")
@Getter
@Entity
public class ScheduleBoardList {
	
    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no; //글번호  PK,AI
    
    
    @Column(name = "title")
    private String title; //제목
    
    @Column(name = "master")
    private String master; //담당자
   
    @Column(name = "master_TEL")
    private String master_TEL; //담당자 전화번호
    
    @Column(name = "master_EMAIL")
    private String master_EMAIL; //담당자 이메일
   
    @Column(name = "content",length =1000)
    private String content; //행사내용
    
    @Column(name = "price")
    private String price; //행사입장료
    
    @Column(name = "place")
    private String place; //행사장소
    
    @Column(name = "organizers")
    private String organizers; //주최측
    
    @Column(name = "create_date", updatable = false)
    private LocalDateTime create_date; //글작성날짜
    
    @Column(name = "imgOFile")
    private String imgOFile; //이미지주소
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private LocalDate start_date; //행사 시작날짜
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate end_date; //행사 마지막날짜
    
    // Getters
    @JsonProperty("no")
    public Long getNo() {
        return no;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("start_date")
    public LocalDate getStart_dated() {
        return start_date;
    }
   
    @JsonProperty("end_date")
    public LocalDate getEnd_dated() {
    	return end_date;
    }
    
    // 댓글 개수 가져오기위한 어노테이션 키값은 pk값만 들어갈수있는거같다.????
    // @Formula 어노테이션 안에 들어가는 SQL 표현식은 데이터베이스 쿼리로 실행
    @Formula("(SELECT COUNT(*) FROM comment WHERE comment.scheduleboard_no = no)")
    private int commentCount;
       
    @Builder
	public ScheduleBoardList(String title, String master, String master_TEL, String master_EMAIL, String content, LocalDate start_date, LocalDate end_date, String price, String place, String organizers, String imgOFile) {
		
    	this.title = title;
		this.master = master;
		this.master_TEL = master_TEL;
		this.master_EMAIL = master_EMAIL;
		this.content = content;
		this.start_date = start_date;
		this.end_date = end_date;
		this.price = price;
		this.place = place;
		this.organizers = organizers;
		this.imgOFile = imgOFile;
	}
    @Builder
	public ScheduleBoardList(Long no,String title, String master, String master_TEL, String master_EMAIL, String content, LocalDate start_date, LocalDate end_date, String price, String place, String organizers, String imgOFile) {
		
    	this.no = no;
    	this.title = title;
		this.master = master;
		this.master_TEL = master_TEL;
		this.master_EMAIL = master_EMAIL;
		this.content = content;
		this.start_date = start_date;
		this.end_date = end_date;
		this.price = price;
		this.place = place;
		this.organizers = organizers;
		this.imgOFile = imgOFile;
	}
    
    @PrePersist
    protected void onCreate() {
        this.create_date = LocalDateTime.now();
    }
//    //@OneToMany 애너테이션은 일대다 관계를 나타낸다. 게시글 하나에 여러 댓글이 달릴 수 있음.
//    //mappedBy 속성을 통해 scheduleboard 필드가 관계의 주인으로 매핑됨을 나타낸다.
//    @OneToMany(mappedBy = "scheduleboard", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    @OrderBy("no asc") // 댓글 정렬
//    private List<Comment> comments;
}
