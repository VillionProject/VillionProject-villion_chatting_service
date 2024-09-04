package org.example.villion_chatting_service.chatRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(
            String senderId, String recipientId, boolean creatNewRoomIfNotExists
    ) {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(chatRoom -> chatRoom.getChatId())
                .or(() -> {
                    if(creatNewRoomIfNotExists) {
                        var chatId = creatChatId(senderId, recipientId); // 채팅방 만들기
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    public List<ChatRoom> getChatRoomId2(
            String senderId, String recipientId
    ) {
        return chatRoomRepository.findBySenderIdOrRecipientId(senderId, recipientId);
    }



    public String creatChatId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }


}
