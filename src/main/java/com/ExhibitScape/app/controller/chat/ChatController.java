package com.ExhibitScape.app.controller.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.ExhibitScape.app.domain.chat.ChatMessage;
import com.ExhibitScape.app.domain.chat.ChatMessageRepository;
import com.ExhibitScape.app.domain.chat.ChatRoom;
import com.ExhibitScape.app.domain.chat.ChatRoomRepository;
import com.ExhibitScape.app.dto.member.MemberDetails;
import com.ExhibitScape.app.service.chat.ChatCache;
import com.ExhibitScape.app.service.chat.ChatRoomService;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class ChatController {

	@Autowired
	private ChatRoomService chatRoomService;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private ChatCache chatCache;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(ChatMessage chatMessage) {
		System.out.println("샌드들어옴");
		System.out.println("chatRoomNo : " + chatMessage.getMessageNo());
		 //채팅방 번호를 사용하여 채팅방 엔티티 찾기
//		ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getMessageNo())
//				.orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
		// 찾은 채팅방 엔티티를 메시지의 채팅방으로 설정
//		chatMessage.setChatRoom(chatRoom);
		ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
		System.out.println("savedMessage : " + chatMessage);
		chatCache.addMessage(savedMessage);
		messagingTemplate.convertAndSend("/topic/room/" + chatMessage.getMessageNo(), savedMessage);

	}

	////// 기존메시지로딩 테스트
	@GetMapping("/chat/messages")
	public ResponseEntity<List<ChatMessage>> getChatMessages(@RequestParam("chatRoomNo") Long chatRoomNo) {
		System.out.println("조회들어옴: " + chatRoomNo);

		return null;// ResponseEntity.ok(messages);
	}


	@GetMapping("/chat")
	public String chat(@RequestParam("roomId") long roomId, Model model, Authentication authentication) {
		String memberNickName = null;
		if (authentication != null && authentication.isAuthenticated()) {
			MemberDetails userDetails = (MemberDetails) authentication.getPrincipal();
			memberNickName = userDetails.getNickname();
			log.info("User '{}' entered the chat room", memberNickName);
		}
		if (memberNickName != null) {
			ChatRoom chatRoom = chatRoomService.findById(roomId);
			model.addAttribute("roomName", chatRoom.getRoomName());
			model.addAttribute("user", memberNickName);
			model.addAttribute("roomId", roomId);
//			String strRoomId = String.valueOf(roomId);
//			chatRoomService.addParticipant(strRoomId,memberNickName);
			return "chat/room";
		}
		return "redirect:/login";
	}

	@PostMapping("/createRoom")
	public RedirectView createRoom(@RequestParam("name") String name, Model model, Authentication authentication) {
		String memberNickName = null;
		String memberId = null;
		if (authentication != null && authentication.isAuthenticated()) {
			MemberDetails userDetails = (MemberDetails) authentication.getPrincipal();
			memberId = userDetails.getUsername();
			memberNickName = userDetails.getNickname();
			log.info("User '{}' 채팅방입장", memberNickName);
		}
		if (memberId != null) {
			ChatRoom chatRoom = chatRoomService.createRoom(name);
			return new RedirectView("/chat?roomId=" + chatRoom.getChatRoomNo());
		}
		return new RedirectView("/login");
	}

	@GetMapping("/chatList")
	public String chatList(Model model, Authentication authentication) {
		List<ChatRoom> chatList = chatRoomService.getAllRooms();
		model.addAttribute("chatList", chatList);
		return "chat/chatList";
	}
	
	
}
