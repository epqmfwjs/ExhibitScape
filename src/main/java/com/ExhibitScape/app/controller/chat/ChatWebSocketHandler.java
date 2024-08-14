//package com.ExhibitScape.app.controller.chat;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import com.ExhibitScape.app.dto.chat.ChatMessage;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Component
//public class ChatWebSocketHandler extends TextWebSocketHandler {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final Map<String, Set<WebSocketSession>> chatRooms = new HashMap<>();
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
//        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);
//        String roomId = chatMessage.getRoomId();
//        TextMessage newMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage));
//
//        chatRooms.getOrDefault(roomId, new HashSet<>()).forEach(webSocketSession -> {
//            try {
//                webSocketSession.sendMessage(newMessage);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        String roomId = session.getUri().getQuery().split("=")[1];
//        chatRooms.computeIfAbsent(roomId, k -> new HashSet<>()).add(session);
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        chatRooms.values().forEach(sessions -> sessions.remove(session));
//    }
//}