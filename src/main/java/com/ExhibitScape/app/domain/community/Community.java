package com.ExhibitScape.app.domain.community;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@JsonIgnoreProperties({"commentList","comLikes"})
public class Community extends BaseTimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//자동증가
	private Integer comId;
	
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
	@Column
    private String memberId;
	@Column(nullable=true)
	private boolean liked;
	@Column(nullable = false, columnDefinition = "int default 0")
	private int likeCount;
	
	@Column(nullable = true)
	private String placeLat;

	@Column(nullable = true)
	private String placeLong;

	@Column(nullable = true)
	private String placeName;
	

	//댓글관련
	@OneToMany(mappedBy = "comId", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("id asc")
	@JsonManagedReference
	private List<ComComment> commentList;
	
	//좋아요 관련
	@ManyToOne
    @JoinColumn(name = "memberId", referencedColumnName = "memberId", insertable = false, updatable = false)
    private MemberDomain member;
	
	@OneToMany(mappedBy = "community", cascade = CascadeType.REMOVE)
	@JsonManagedReference
	private List<ComLike> comLikes;
	

	

}
