package com.ExhibitScape.app.dto.community;

import java.util.Date;

import lombok.Data;

@Data
public class ComLikeDTO {
	private Integer comLikeId;
	private Integer comId;
	private String memberId;
	private boolean comLikeCheck;
	private Date postDate;

	
}
