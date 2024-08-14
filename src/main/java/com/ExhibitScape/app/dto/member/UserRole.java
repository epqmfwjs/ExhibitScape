package com.ExhibitScape.app.dto.member;

import lombok.Getter;

@Getter
public enum UserRole {
	
	//ADMIN상수는 "ROLE_ADMIN"값을 가지고 있다
	//USER상수는 "ROLE_USER"값을 가지고 있다
	ADMIN("ROLE_ADMIN"), 
	USER("ROLE_USER");
	

	UserRole(String value){
		this.value = value;
	}
	private String value;
	
}
