package com.ExhibitScape.app.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ExhibitScape.app.dto.member.JoinDTO;
import com.ExhibitScape.app.service.member.JoinService;

@RestController
public class RestJoinController {

    @Autowired
    private JoinService joinService;

    @PostMapping("/idcheck")
    public ResponseEntity<Boolean> idcheck(@RequestBody JoinDTO joinDTO) {
        String memberId = joinDTO.getMemberId();
        boolean isDuplicate = joinService.checkDuplicateMemberId(memberId);
        return ResponseEntity.ok(!isDuplicate);
    }

    @PostMapping("/nicknamecheck")
    public ResponseEntity<Boolean> nicknameCheck(@RequestBody JoinDTO joinDTO) {
        String nickname = joinDTO.getNickname();
        boolean isDuplicate = joinService.checkDuplicateNickname(nickname);
        return ResponseEntity.ok(!isDuplicate);
    }

    @PostMapping("/emailcheck")
    public ResponseEntity<Boolean> emailCheck(@RequestBody JoinDTO joinDTO) {
        String email = joinDTO.getEmail();
        boolean isDuplicate = joinService.checkDuplicateEmail(email);
        return ResponseEntity.ok(!isDuplicate);
    }
}