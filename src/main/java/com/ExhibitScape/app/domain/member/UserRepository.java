package com.ExhibitScape.app.domain.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<MemberDomain, Integer> {

	Optional<MemberDomain> findByMemberId(String MemberId);

	MemberDomain findByMemberNo(Integer memberNo);

	MemberDomain findByMemberIdAndEmail(String memberId, String email);

	boolean existsByMemberId(String memberId);

	boolean existsByNickname(String nickname);

	boolean existsByEmail(String email);

}