package com.ExhibitScape.app.domain.community;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Community extends BaseTimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//자동증가
	private Integer comId;
	
	@Column(nullable=true)
	private String memberId;
	@Column
	private String comTitle;
	@Column(columnDefinition = "TEXT" )
	private String comContent;
	@Column(nullable=true)
	private String comImgSname;
	@Column(nullable=true)
	private String comImgPath;
	@Column
	private String comCategory;
	@Column(nullable=true)
	private Long comHits;
	@Column(nullable=true)
	private String comTag;
	
	
	//댓글관련
	@OneToMany(mappedBy = "comId", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@OrderBy("id asc")
	private List<ComComment> commentList;

	

}
