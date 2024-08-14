package com.ExhibitScape.app.service.chat;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.domain.chat.ChatMessageRepository;
import com.ExhibitScape.app.domain.chat.ChatRoom;
import com.ExhibitScape.app.domain.chat.ChatRoomRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatRoomService {
	private final ConcurrentHashMap<Long, AtomicInteger> userCountMap = new ConcurrentHashMap<>();

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
    // roomId별 참여자 목록 관리
    private final Map<String, Set<String>> roomParticipantsMap = new ConcurrentHashMap<>();
	
	@Autowired
	public ChatRoomService(ChatRoomRepository chatRoomRepository) {
		this.chatRoomRepository = chatRoomRepository;
	}

	public ChatRoom createRoom(String roomName) {
		ChatRoom chatRoom = new ChatRoom();
		System.out.println("방네임 : " + roomName);
		chatRoom.setRoomName(roomName);
		return chatRoomRepository.save(chatRoom);
	}

	public List<ChatRoom> getAllRooms() {
		return chatRoomRepository.findAll();
	}

	public ChatRoom findById(Long no) {
		return chatRoomRepository.findById(no).orElse(null);
	}
	//채팅방 참여인원 관련메소드
    
	// 참여자 추가
    public void addParticipant(String roomId, String name) {
        roomParticipantsMap.computeIfAbsent(roomId, k -> Collections.synchronizedSet(new HashSet<>())).add(name);
    }

    // 참여자 제거
    @Transactional
    public void removeParticipant(String roomId, String name) {
    	Long LongRoomId = Long.valueOf(roomId);
        if (roomParticipantsMap.containsKey(roomId)) {
            roomParticipantsMap.get(roomId).remove(name);
            if (roomParticipantsMap.get(roomId).isEmpty()) {
                roomParticipantsMap.remove(roomId); // 방에 아무도 없으면 방 삭제
                chatRoomRepository.deleteBychatRoomNo(LongRoomId);
            }
        }
    }

    // 특정 방의 참여자 목록 가져오기
    public Set<String> getParticipants(String roomId) {
    	System.out.println("참여자목록 서비스 들어옴 : " + roomId);
    	return roomParticipantsMap.getOrDefault(roomId, Collections.emptySet());
    } 
        // 메서드 구현...
//        Set<String> participants = roomParticipantsMap.getOrDefault(roomId, Collections.emptySet());// 참여자 목록 조회 로직
//        
//        Iterator<String> iterator = participants.iterator();
//        while(iterator.hasNext()) {
//            String element = iterator.next();
//            System.out.println(element);
//        }        
   
/*
	public void userJoined(Long chatRoomNo) {
		userCountMap.computeIfAbsent(chatRoomNo, k -> new AtomicInteger(0)).incrementAndGet();
	}

	public void userLeft(Long chatRoomNo) {
		AtomicInteger count = userCountMap.get(chatRoomNo);
		if (count != null && count.decrementAndGet() == 0) {

			// 마지막 사용자가 떠나면 채팅방 삭제
			chatRoomRepository.deleteById(chatRoomNo);
			userCountMap.remove(chatRoomNo);
		}
	}
	*/

}