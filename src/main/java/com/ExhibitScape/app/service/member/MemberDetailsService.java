package com.ExhibitScape.app.service.member;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.common.DataNotFoundException;
import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.domain.member.UserRepository;
import com.ExhibitScape.app.dto.member.MemberDetails;

@Service
public class MemberDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
	private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Optional<MemberDomain> optionalMemberData = userRepository.findByMemberId(memberId);

        if (optionalMemberData.isPresent()) {
            MemberDomain memberData = optionalMemberData.get();
            return new MemberDetails(memberData);
        } else {
            throw new UsernameNotFoundException("Member not found with memberId: " + memberId);
        }
    }
    
    public MemberDomain getUser(String memberId) {
    	Optional<MemberDomain> memberDomain = userRepository.findByMemberId(memberId);
    	if(memberDomain.isPresent()) {
    		return memberDomain.get();
    	}else {
    		throw new DataNotFoundException("Member not found with memberId: " + memberId);
    	}
    }
}






