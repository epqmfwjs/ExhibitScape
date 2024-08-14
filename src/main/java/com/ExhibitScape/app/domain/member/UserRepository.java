package com.ExhibitScape.app.domain.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<MemberDomain, Integer> {

//	 boolean existsByMemberId(String memberId);
//	 boolean existsByNickname(String nickname);
//	 boolean existsByEmail(String email);
//	 
//	
//	 MemberDomain findByMemberId(String memberId);

	 boolean existsByMemberId(String memberId);
	 boolean existsByNickname(String nickname);
	 boolean existsByEmail(String email);
	 
	 Optional<MemberDomain> findByMemberId(String MemberId);
	
}
