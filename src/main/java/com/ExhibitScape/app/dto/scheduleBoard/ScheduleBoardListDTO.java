package com.ExhibitScape.app.dto.scheduleBoard;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class ScheduleBoardListDTO<T> {
   
	 private List<T> data;
	 private Page<?> pageInfo;

}
