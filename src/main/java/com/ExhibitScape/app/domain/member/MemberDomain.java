package com.ExhibitScape.app.domain.member;

import com.ExhibitScape.app.dto.admin.MemberDTO;

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
	
	
	@Column(unique = true, nullable = false, length = 12)
	private String memberId;
	
    @Column(nullable = false)
	private String password;
	
    @Column(unique = true, nullable = false)
	private String nickname;
	
    @Column(unique = true, nullable = false)
    private String email;
    
	private String role;
	
//	public static MemberDomain toUpdateMemberEntity(MemberDTO memberDTO) {
//	MemberDomain memberEntity = new MemberDomain();
//	memberEntity.setMemberId(memberDTO.getMemberId());
//	memberEntity.setNickname(memberDTO.getNickname());
//	memberEntity.setRole(memberDTO.getRole());
//	return memberEntity;
//}
	
	public static MemberDomain toUpdateMemberEntity(MemberDTO memberDTO) {
	    MemberDomain memberDomain = new MemberDomain();
	    memberDomain.setMemberNo(memberDTO.getMemberNo());
	    memberDomain.setMemberId(memberDTO.getMemberId());
	    memberDomain.setPassword(memberDTO.getPassword());
	    memberDomain.setNickname(memberDTO.getNickname());
	    memberDomain.setEmail(memberDTO.getEmail());
	    memberDomain.setRole(memberDTO.getRole());
	    return memberDomain;
	}
	
	private static MemberDomain toMemberEntity(MemberDTO memberDTO) {
		MemberDomain memberEntity = new MemberDomain();
		memberEntity.setMemberNo(memberDTO.getMemberNo());
		memberEntity.setMemberId(memberDTO.getMemberId());
		memberEntity.setNickname(memberDTO.getNickname());
		memberEntity.setRole(memberDTO.getRole());
		return memberEntity;
	}
}
