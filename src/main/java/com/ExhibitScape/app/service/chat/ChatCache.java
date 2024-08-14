package com.ExhibitScape.app.service.chat;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ExhibitScape.app.domain.chat.ChatMessage;
import com.ExhibitScape.app.domain.chat.ChatMessageRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import groovyjarjarpicocli.CommandLine.ExecutionException;

@Service
public class ChatCache {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private final LoadingCache<Long, ChatMessage> chatMessages;

    public ChatCache() {
        chatMessages = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<Long, ChatMessage>() {
                            public ChatMessage load(Long key) {
                                // 실제 데이터베이스에서 메시지 로드
                                return chatMessageRepository.findById(key).orElse(null);
                            }
                        });
    }

    public ChatMessage getMessage(Long id) throws java.util.concurrent.ExecutionException {
        try {
            return chatMessages.get(id);
        } catch (ExecutionException e) {
            return null;
        }
    }

    public void addMessage(ChatMessage chatMessage) {
        // ID가 null인 경우는 캐시에 추가하지 않음
        if (chatMessage.getMessageNo() != null) {
            chatMessages.put(chatMessage.getMessageNo(), chatMessage);
        }
    }

    public void removeMessage(Long id) {
        chatMessages.invalidate(id);
    }
    

}
