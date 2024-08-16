package com.ExhibitScape.app.domain.community;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComComment extends BaseTimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//자동증가
	private Integer comComId;
	
	@Column(nullable=true)
	private String memberId;
	@Column(columnDefinition = "TEXT", nullable = false)
	private String comContent;
	
	@ManyToOne
	@JoinColumn(name="comId")
	@JsonBackReference
	private Community comId;
	
	@Column(nullable=true)
	private String imgRandom;
	
	@Override
	public String toString() {
		return "ComComment [comComId=" + comComId + ", memberId=" + memberId + ", comContent=" + comContent
				+ ", comId=" + comId + "]";
	}
	
	

}
