package com.ExhibitScape.app.dto.community;

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
	private Integer comId;
	private String imgRandom;
}
