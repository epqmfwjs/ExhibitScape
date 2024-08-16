package com.ExhibitScape.app.dto.admin;

import com.ExhibitScape.app.domain.member.MemberDomain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {

    private Integer memberNo;
    
	private String memberId;
	
	private String password;

	private String nickname;
	
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    
	private String role;
	
    public static MemberDTO toMemberDTO(MemberDomain memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberNo(memberEntity.getMemberNo());
        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setPassword(memberEntity.getPassword());
        memberDTO.setNickname(memberEntity.getNickname());
        memberDTO.setEmail(memberEntity.getEmail());
        memberDTO.setRole(memberEntity.getRole());
        return memberDTO;
    }
}
