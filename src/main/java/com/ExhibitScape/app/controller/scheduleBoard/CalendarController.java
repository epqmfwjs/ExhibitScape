package com.ExhibitScape.app.controller.scheduleBoard;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ExhibitScape.app.domain.scheduleBoard.ScheduleBoardList;
import com.ExhibitScape.app.domain.scheduleBoard.ScheduleBoardListSpecification;
import com.ExhibitScape.app.service.scheduleBoard.ScheduleBoardService;


@RestController
@RequestMapping("/calendar")
public class CalendarController {

	@Autowired
	private ScheduleBoardService boardService;
	
    @GetMapping("/allCalendar")
    @ResponseBody
    public List<ScheduleBoardList> list() {
    	System.out.println("calendar controller 들어옴");
        List<ScheduleBoardList> DBlist = boardService.calendarList();
        return DBlist;
    }
    
    @GetMapping("/calendarDate")
    public List<ScheduleBoardList> getEvents(@RequestParam("start_date") LocalDate start_date, @RequestParam("end_date") LocalDate end_date) {
    	
        List<ScheduleBoardList> events = boardService.findAllBystart_dateBetween(start_date, end_date);
        return events;
    }
    
    @GetMapping("/calendarSearch")
    public List<ScheduleBoardList> getCalendarSearch(@RequestParam("keyword") String keyword) {
    	boardService.updateSearchCount(keyword);
    	System.out.println("캘린더 검색 키워드 : " + keyword);
    	Specification<ScheduleBoardList> spec = ScheduleBoardListSpecification.titleOrContentContains(keyword);
        List<ScheduleBoardList> events = boardService.findAll(spec);
        return events;
    }
}