package com.ExhibitScape.app.service.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.domain.member.UserRepository;

@Service
public class MyPageService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MyPageService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberDomain getMemberInfo(String memberId) {
        return userRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    @Transactional
    public void updateNickname(String memberId, String nickname) {
        MemberDomain member = getMemberInfo(memberId);
        member.setNickname(nickname);
        userRepository.save(member);
    }

    @Transactional
    public void updateEmail(String memberId, String email) {
        MemberDomain member = getMemberInfo(memberId);
        member.setEmail(email);
        userRepository.save(member);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isCurrentPasswordCorrect(String memberId, String currentPassword) {
        MemberDomain member = getMemberInfo(memberId);
        return passwordEncoder.matches(currentPassword, member.getPassword());
    }

    @Transactional
    public void changePassword(String memberId, String newPassword) {
        MemberDomain member = getMemberInfo(memberId);
        member.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(member);
    }
}