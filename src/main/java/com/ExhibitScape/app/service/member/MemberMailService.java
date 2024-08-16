package com.ExhibitScape.app.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.domain.member.MemberDomain;
import com.ExhibitScape.app.domain.member.UserRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class MemberMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    public boolean checkMemberInfo(String memberId, String email) {
        MemberDomain member = userRepository.findByMemberIdAndEmail(memberId, email);
        return member != null;
    }
    
    public boolean sendTemporaryPassword(String memberId, String email) {
        // 아이디와 이메일로 회원 조회
        MemberDomain member = userRepository.findByMemberIdAndEmail(memberId, email);

        if (member != null) {
            // 임시 비밀번호 생성
            String temporaryPassword = generateTemporaryPassword();

            // 생성된 임시 비밀번호로 회원 정보 업데이트
            member.setPassword(temporaryPassword);
            userRepository.save(member);

            // 회원의 이메일로 임시 비밀번호 발송
            return sendEmail(member.getEmail(), temporaryPassword);
        }

        return false;
    }

    private boolean sendEmail(String recipientEmail, String temporaryPassword) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setSubject("ExhibitScape 임시 비밀번호 발급");
            String content = "임시 비밀번호: " + temporaryPassword;
            mimeMessageHelper.setText(content);

            javaMailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateTemporaryPassword() {
        String[] charSet = new String[] {
            "0","1","2","3","4","5","6","7","8","9",
            "a","b","c","d","e","f","g",
            "A","B","C","D","E","F","G"
        };

        String tempPW = "";

        for(int i = 0; i<4; i++) {
            int randIndex = (int)(Math.random() * charSet.length);
            tempPW += charSet[randIndex];
        }
        return tempPW;
    }
}