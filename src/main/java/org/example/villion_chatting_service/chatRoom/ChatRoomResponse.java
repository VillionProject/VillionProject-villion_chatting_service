package org.example.villion_chatting_service.chatRoom;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomResponse {
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
}
