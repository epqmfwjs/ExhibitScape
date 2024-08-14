package com.ExhibitScape.app.dto.scheduleBoard;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardVO {
	
	private Long no; // 
    
    private String title; //제목
    
    private String master; //담당자

    private String master_TEL; //담당자
    
    private String master_EMAIL; //담당자
   
    private String content; //행사내용
    
    private String price; //행사내용
    
    private LocalDate start_date; //행사시작일
    
    private LocalDate end_date; //행사종료일
    
    private String place; //행사장소
    
    private String organizers; //주최자
     
    private String imgOFile; //이미지주소
}
