package com.ExhibitScape.app.dto.member;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ExhibitScape.app.domain.member.MemberDomain;

public class MemberDetails implements UserDetails{

	private MemberDomain memberDomain;
	
	public MemberDetails(MemberDomain memberDomain) {
		this.memberDomain = memberDomain;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collection = new ArrayList<>();

	    collection.add(new GrantedAuthority() {

	      @Override
	      public String getAuthority() {

	           return memberDomain.getRole();
	          }
	     });

	     return collection;
	    }

	@Override
	public String getPassword() {
		return memberDomain.getPassword();
	}

	@Override
	public String getUsername() {
	    return memberDomain.getMemberId();
	}
	
	
	public String getNickname() {
	    return memberDomain.getNickname();
	}
	
	public String getEmail() {
	    return memberDomain.getEmail();
	}
	
	
	public Integer getMemberNo() {
	    return memberDomain.getMemberNo();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	
	
}