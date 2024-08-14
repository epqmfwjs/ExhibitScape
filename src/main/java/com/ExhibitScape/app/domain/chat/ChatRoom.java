package com.ExhibitScape.app.domain.chat;
import java.util.List;

import org.hibernate.internal.build.AllowNonPortable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllowNonPortable
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ChatRoom {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomNo;
    private String roomName;
    
//    @JsonIgnore //직렬화 과정에서 트렌젝셔널 특정필드 무시하게하는 jackson라이브러리 어노테이션
//    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, orphanRemoval = true,fetch =  FetchType.LAZY)
//    private List<ChatMessage> chatMessages; // 채팅 메시지 목록


    public ChatRoom(String roomName) {
        this.roomName = roomName;
    }
}