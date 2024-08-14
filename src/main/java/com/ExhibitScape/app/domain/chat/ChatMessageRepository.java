package com.ExhibitScape.app.domain.chat;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

//	@Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.chatRoomNo = :chatRoomNo")
//    List<ChatMessage> findByChatRoomNo(@Param("chatRoomNo") Long chatRoomNo);
	

}
