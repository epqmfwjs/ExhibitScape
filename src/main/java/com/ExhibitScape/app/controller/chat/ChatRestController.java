package com.ExhibitScape.app.controller.chat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ExhibitScape.app.domain.chat.ParticipantInfo;
import com.ExhibitScape.app.dto.member.MemberDetails;
import com.ExhibitScape.app.service.chat.ChatRoomService;

@RestController
public class ChatRestController {
    @Autowired
    private ChatRoomService chatRoomService;

    @Value("${SOCKET_URL}")
    private String socketUrl;
    
    // 특정 방의 참여자 목록 조회
    @GetMapping("/chat/participants")
    public List<String> getParticipants(@RequestParam("roomId") String roomId,Authentication authentication) {

        MemberDetails userDetails = (MemberDetails) authentication.getPrincipal();
		//처음들어갈떄 roomId를 K , 닉네임을 V 로 맵으로관리  서비스쪽으로 보내서 로직처리
		String memberNickName = userDetails.getNickname();
		chatRoomService.addParticipant(roomId,memberNickName);
        
		Set<String> participants = chatRoomService.getParticipants(roomId);
        return participants.stream().collect(Collectors.toList());
    }
    
    @PostMapping("/chat/deleteParticipants")
    public ResponseEntity<?> deleteParticipants(@RequestBody ParticipantInfo participantInfo) {
        	String roomId = participantInfo.getRoomId();
        	String sender = participantInfo.getSender();

    	chatRoomService.removeParticipant(roomId,sender);
        return ResponseEntity.ok().body("참가자 삭제 성공");
    }

    // 아이피 암호화 .env 사용
    @GetMapping("/api/socket-url")
    public String getSocketUrl() {
        return socketUrl;
    }


}



