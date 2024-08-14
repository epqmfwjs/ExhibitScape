package com.ExhibitScape.app.service.member;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	//로그인 아이디 정보
	public String userId() {
		
		return SecurityContextHolder.getContext().getAuthentication().getName();
		
	}
	
	
	//로그인한 사용자 등급 정보
	public String userGrade() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iter = authorities.iterator();
		GrantedAuthority auth = iter.next();
		String role = auth.getAuthority();
		return role;
		
	}
}
