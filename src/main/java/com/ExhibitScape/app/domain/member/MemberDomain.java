package com.ExhibitScape.app.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class MemberDomain {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberNo;
	
	@Column(unique = true, nullable = false, length = 15)
	private String memberId;
	
    @Column(nullable = false)
	private String password;
	
    @Column(unique = true, nullable = false, length = 12)
	private String nickname;
	
    @Column(unique = true, nullable = false)
    private String email;
    
	private String role;
}
