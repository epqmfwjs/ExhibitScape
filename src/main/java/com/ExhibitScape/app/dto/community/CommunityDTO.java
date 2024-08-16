package com.ExhibitScape.app.dto.community;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.ExhibitScape.app.domain.community.ComComment;
import com.ExhibitScape.app.domain.member.MemberDomain;

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
	private boolean liked;
    private int likeCount;
    private String placeLat;
    private String placeLong;
    private String placeName;
    
	
	private List<ComComment> commentList;
	private Set<MemberDomain> rec;
	@Override
	public String toString() {
		return "CommunityDTO [comId=" + comId + ", memberId=" + memberId + ", comTitle=" + comTitle + ", comContent="
				+ comContent + ", postDate=" + postDate + ", comImgSname=" + comImgSname + ", comImgPath=" + comImgPath
				+ ", comCategory=" + comCategory + ", comHits=" + comHits + ", comTag=" + comTag + ", liked=" + liked
				+ ", likeCount=" + likeCount + ", placeLat=" + placeLat + ", placeLong=" + placeLong + ", placeName="
				+ placeName + ", commentList=" + commentList + ", rec=" + rec + "]";
	}

	
}
