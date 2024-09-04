package org.example.villion_chatting_service.chatRoom;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
    List<ChatRoom> findBySenderIdOrRecipientId(String senderId, String recipientId);


}
