package com.ExhibitScape.app.domain.community;

import java.util.Date;

import com.ExhibitScape.app.domain.member.MemberDomain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ComLike {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer comLikeId;
	
	@ManyToOne
	@JoinColumn(name="comId")
	private Community community;
	
	@ManyToOne
	@JoinColumn(name="memberId")
	private MemberDomain memberId;
	
	@Column(nullable = true)
	private boolean comLikeCheck;
	@Column(nullable = true)
	private Date postDate;
	
	@Override
	public String toString() {
		return "ComLike [comLikeId=" + comLikeId + ", community=" + community + ", memberId=" + memberId
				+ ", comLikeCheck=" + comLikeCheck + ", postDate=" + postDate + "]";
	}
	
	

}
