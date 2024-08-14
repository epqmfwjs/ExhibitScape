package com.ExhibitScape.app.dto.community;

import com.ExhibitScape.app.domain.community.Community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ComCommentDTO {
	private Integer comComId;
	private String memberId;
	private String comContent;
	private String postDate;
	private Community community;
	private Community comId;

}
