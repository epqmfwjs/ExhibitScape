package com.ExhibitScape.app.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinDTO {
	//회원가입페이지의 유효성검사 담당 객체
	@NotBlank(message="아이디를 입력해주세요")
	@Pattern(regexp = "^[a-z0-9]{4,12}$", message = "아이디는 영문 소문자와 숫자 4~12자리여야 합니다.")
	private String memberId;
	
	@NotBlank(message="비밀번호를 입력해주세요")
	private String password1;
	
	@NotBlank(message = "비밀번호 확인은 필수입니다")
	private String password2; //비번확인용
	
	@NotBlank(message = "닉네임을 입력해주세요")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
	private String nickname;
	
	@NotBlank(message = "이메일을 입력해주세요")
	@Email(message = "이메일 형식이 올바르지 않습니다")
	private String email;    //이메일

	@Override
	public String toString() {
		return "JoinDTO [memberId=" + memberId + ", password1=" + password1 + ", password2=" + password2 + ", nickname="
				+ nickname + ", email=" + email + "]";
	}
}
