package com.ExhibitScape.app.service.member;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.domain.member.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberDeleteService {

    @Autowired
    private UserRepository userRepository;

    public MemberDomain findByMemberId(String memberId) {
        return userRepository.findByMemberId(memberId).orElse(null);
    }

    public void deleteMember(Integer memberNo) {
        userRepository.deleteById(memberNo);
    }
    
    public boolean checkMemberInfo(String memberId, String email) {
        MemberDomain member = userRepository.findByMemberIdAndEmail(memberId, email);
        return member != null;
    }
}
