package com.ExhibitScape.app.controller.chat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
    

    
    // 특정 방의 참여자 목록 조회

    
    @GetMapping("/chat/participants")
    public List<String> getParticipants(@RequestParam("roomId") String roomId,Authentication authentication) {
        System.out.println("참여자목록확인컨트롤러들어옴 : " + roomId);
		MemberDetails userDetails = (MemberDetails) authentication.getPrincipal();
		//처음들어갈떄 roomId를 K , 닉네임을 V 로 맵으로관리  서비스쪽으로 보내서 로직처리
		String memberNickName = userDetails.getNickname();
		//String strRoomId = String.valueOf(roomId);
		chatRoomService.addParticipant(roomId,memberNickName);
        
		Set<String> participants = chatRoomService.getParticipants(roomId);
        return participants.stream().collect(Collectors.toList());
    }
    
    @PostMapping("/chat/deleteParticipants")
    public ResponseEntity<?> deleteParticipants(@RequestBody ParticipantInfo participantInfo) {
        	String roomId = participantInfo.getRoomId();
        	String sender = participantInfo.getSender();
    	System.out.println("채팅방멤버 삭제 컨트롤러 들어옴 : " + roomId +" : "+ sender);
    	// participantInfo 객체를 사용하여 로직 처리
        // 예: 참가자 삭제, 데이터베이스 업데이트 등
    	chatRoomService.removeParticipant(roomId,sender);
        // 처리 후 성공 응답 반환
        return ResponseEntity.ok().body("참가자 삭제 성공");
    }
}

//    @GetMapping("/chatList")
//    public List<ChatRoom> getAllRooms() {
//        return chatRoomService.getAllRooms();
//    }

