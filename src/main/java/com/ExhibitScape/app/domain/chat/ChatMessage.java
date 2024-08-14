package com.ExhibitScape.app.domain.chat;

import org.hibernate.internal.build.AllowNonPortable;

import com.ExhibitScape.app.domain.scheduleBoard.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllowNonPortable
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ChatMessage extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageNo;
	private String sender;
	private String content;
	
//    @ManyToOne
//    @JoinColumn(name = "chatRoomNo")
//    private ChatRoom chatRoom; // 채팅방에 대한 참조 추가
	
//    public ChatMessage(Long chatRoomNo) {
//        this.chatRoom = chatRoomNo;
//    }
}   


