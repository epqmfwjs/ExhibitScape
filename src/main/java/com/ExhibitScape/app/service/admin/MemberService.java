package com.ExhibitScape.app.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ExhibitScape.app.common.DataNotFoundException;
import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.domain.member.UserRepository;
import com.ExhibitScape.app.dto.admin.MemberDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final UserRepository userRepository;

	public List<MemberDTO> findAll() {
		List<MemberDomain> memberEntityList = userRepository.findAll();
		List<MemberDTO> memberDTOList = new ArrayList<>();
		for (MemberDomain memberEntity : memberEntityList) {
			memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
		}
		return memberDTOList;
	}

	public MemberDTO findById(Integer memberNo) {
		Optional<MemberDomain> optionalMemberEntity = userRepository.findById(memberNo);
		if (optionalMemberEntity.isPresent()) {
			return MemberDTO.toMemberDTO(optionalMemberEntity.get());
		} else {
			return null;
		}
	}

	public MemberDTO updateForm(String memberId) {
		Optional<MemberDomain> optionalMemberEntity = userRepository.findByMemberId(memberId);
		if (optionalMemberEntity.isPresent()) {
			return MemberDTO.toMemberDTO(optionalMemberEntity.get());
		} else {
			return null;
		}
	}

	public void update(MemberDTO memberDTO) {
	    userRepository.save(MemberDomain.toUpdateMemberEntity(memberDTO));
	}

	public void deleteById(Integer memberNo) {
		userRepository.deleteById(memberNo);
	}

	// 특정 user조회
	public Optional<MemberDomain> getUser(String memberId) {
		Optional<Optional<MemberDomain>> member = Optional.of(userRepository.findByMemberId(memberId));
		if (member.isPresent()) {
			return member.get();
		} else {
			throw new DataNotFoundException("siteUser not found.");
		}

	}

}