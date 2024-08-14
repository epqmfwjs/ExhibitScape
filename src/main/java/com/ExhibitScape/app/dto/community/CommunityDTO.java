package com.ExhibitScape.app.dto.community;

import java.time.LocalDateTime;
import java.util.List;

import com.ExhibitScape.app.domain.community.ComComment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDTO {

	
	private Integer comId;
	private String memberId;
	private String comTitle;
	private String comContent;
	private LocalDateTime postDate;
	private String comImgSname;
	private String comImgPath;
	private String comCategory;
	private Long comHits;
	private String comTag;
	private List<ComComment> commentList;
	
}
