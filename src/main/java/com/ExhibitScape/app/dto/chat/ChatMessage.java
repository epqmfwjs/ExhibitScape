package com.ExhibitScape.app.dto.chat;

import lombok.Getter;
import lombok.Setter;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatMessage {
    private String roomId;
    private String sender;
    private String content;

}
