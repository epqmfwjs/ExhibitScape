package com.ExhibitScape.app.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	void deleteBychatRoomNo(Long longRoomId);

}
