package com.ExhibitScape.app.service.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.domain.member.UserRepository;

@Service
public class MyPagePWChangeService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MyPageService myPageService;

    public MyPagePWChangeService(UserRepository userRepository, PasswordEncoder passwordEncoder, MyPageService myPageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.myPageService = myPageService;
    }

    public boolean isCurrentPasswordCorrect(String memberId, String currentPassword) {
        MemberDomain member = myPageService.getMemberInfo(memberId);
        return passwordEncoder.matches(currentPassword, member.getPassword());
    }

    @Transactional
    public void changePassword(String memberId, String newPassword) {
        MemberDomain member = myPageService.getMemberInfo(memberId);
        member.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(member);
    }
}