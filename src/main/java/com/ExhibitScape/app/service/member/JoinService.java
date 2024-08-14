package com.ExhibitScape.app.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.dto.member.JoinDTO;
import com.ExhibitScape.app.domain.member.UserRepository;

@Service
public class JoinService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public MemberDomain joinProcess(String memberId, String password,
	                                String nickname, String email) {
		

		MemberDomain memberDomain = new MemberDomain();
		
		memberDomain.setMemberId(memberId);
		memberDomain.setPassword(bCryptPasswordEncoder.encode(password));
		memberDomain.setNickname(nickname);
		memberDomain.setEmail(email);
		memberDomain.setRole("ROLE_USER");
		
		userRepository.save(memberDomain);
		return memberDomain;
	}
	
	 public boolean checkDuplicateMemberId(String memberId) {
	        return userRepository.existsByMemberId(memberId);
	    }

	    public boolean checkDuplicateNickname(String nickname) {
	        return userRepository.existsByNickname(nickname);
	    }

	    public boolean checkDuplicateEmail(String email) {
	        return userRepository.existsByEmail(email);
	    }
	
}
