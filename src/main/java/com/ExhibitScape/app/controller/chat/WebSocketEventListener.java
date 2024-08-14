package com.ExhibitScape.app.controller.chat;



import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class WebSocketEventListener {

    // 채팅방 ID를 키로 하고, 사용자 수를 값으로 하는 맵
    private final ConcurrentMap<String, Integer> chatRoomUserCounts = new ConcurrentHashMap<>();

    // 사용자가 채팅방에 들어올 때 호출
    public void userJoinedChatRoom(String chatRoomId) {
        chatRoomUserCounts.merge(chatRoomId, 1, Integer::sum);
    }

    // 사용자가 채팅방을 나갈 때 호출
    public void userLeftChatRoom(String chatRoomId) {
        chatRoomUserCounts.merge(chatRoomId, -1, Integer::sum);
        if (chatRoomUserCounts.get(chatRoomId) <= 0) {
            chatRoomUserCounts.remove(chatRoomId);
            deleteChatRoom(chatRoomId);
        }
    }

    // 채팅방 삭제 로직
    private void deleteChatRoom(String chatRoomId) {
        // 채팅방 삭제 로직 구현
        System.out.println("Chat room " + chatRoomId + " deleted.");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String chatRoomId = (String) headerAccessor.getSessionAttributes().get("chatRoomId");
        if (chatRoomId != null) {
            userLeftChatRoom(chatRoomId);
        }
    }
}
