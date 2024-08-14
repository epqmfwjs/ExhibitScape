package com.ExhibitScape.app.controller.scheduleBoard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ExhibitScape.app.service.scheduleBoard.ScheduleBoardService;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private ScheduleBoardService boardService;

    @ModelAttribute("popularKeywords")
    public List<String> populatePopularKeywords() {
        return boardService.getPopularSearchKeywords(5); // 상위 5개의 인기 검색어를 가져옵니다.
    }
}
