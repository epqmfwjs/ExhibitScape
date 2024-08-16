package com.ExhibitScape.app.domain.community;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ExhibitScape.app.domain.member.MemberDomain;

public interface ComLikeRepository extends JpaRepository<ComLike, Integer>{
	Optional<ComLike> findByCommunityAndMemberDomain(Community community, MemberDomain memberDomain);
    boolean existsByCommunityAndMemberDomain(Community community, MemberDomain memberDomain);
    int countByCommunity(Community community);
    List<ComLike> findByMemberDomain_memberId(String memberId);
}
