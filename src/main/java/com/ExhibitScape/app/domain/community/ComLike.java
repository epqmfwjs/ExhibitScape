package com.ExhibitScape.app.domain.community;

import java.time.LocalDateTime;
import java.util.Date;

import com.ExhibitScape.app.domain.member.MemberDomain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"com_id", "member_id"}))
public class ComLike {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer comLikeId;
	
	
	@Column(nullable = true)
	private boolean comLikeCheck;
	
	@Column(nullable = true)
	private LocalDateTime postDate;
	
    @ManyToOne
    @JoinColumn(name = "com_id")
    private Community community;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberDomain memberDomain;
	
	@Override
	public String toString() {
		return "ComLike [comLikeId=" + comLikeId + ", community=" + community + ", memberDomain=" + memberDomain
				+ ", comLikeCheck=" + comLikeCheck + ", postDate=" + postDate + "]";
	}
	
	

}
