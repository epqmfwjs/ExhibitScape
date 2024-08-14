package com.ExhibitScape.app.service.member;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.domain.member.UserRepository;
import com.ExhibitScape.app.dto.member.MemberDetails;

@Service
public class MemberDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        //MemberDomain memberData = userRepository.findByMemberId(memberId);
        Optional<MemberDomain> optionalMemberData = userRepository.findByMemberId(memberId);

        
//        if (memberData != null) {
//            return new MemberDetails(memberData);
//        }
//        
//        throw new UsernameNotFoundException("Member not found with memberId: " + memberId);

        if (optionalMemberData.isPresent()) {
            MemberDomain memberData = optionalMemberData.get();
            return new MemberDetails(memberData);
        } else {
            throw new UsernameNotFoundException("Member not found with memberId: " + memberId);
        }
        
    }
}





