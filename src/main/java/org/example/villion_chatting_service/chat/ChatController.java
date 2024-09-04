package org.example.villion_chatting_service.chat;

import lombok.RequiredArgsConstructor;
import org.example.villion_chatting_service.chatRoom.ChatRoom;
import org.example.villion_chatting_service.chatRoom.ChatRoomResponse;
import org.example.villion_chatting_service.chatRoom.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;


    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);

        // 특정 사용자/수신자ID의 대기열에 메시지 게시
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification( // 채팅 알림
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

    // 채팅 보여주기
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                 @PathVariable String recipientId) {

        return ResponseEntity
                    .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    // 내 채팅방 불러오기
//    @GetMapping("/room/{userId}")
//    public ResponseEntity<List<ChatRoom>> findMyChatRoom(@PathVariable String userId) {
//
//        List<ChatRoom> chatRoomId2 = chatRoomService.getChatRoomId2(userId, userId);
//
//        return ResponseEntity
//                .ok(chatRoomId2);
//    }
    @GetMapping("/room/{userId}")
    public ResponseEntity<List<ChatRoom>> findMyChatRoom(@PathVariable String userId) {

        List<ChatRoom> chatRoomId2 = chatRoomService.getChatRoomId2(userId, userId);

        // 중복된 chatId를 가지는 객체들을 제거합니다.
        List<ChatRoom> filteredChatRooms = chatRoomId2.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(ChatRoom::getChatId, Function.identity(), (existing, replacement) -> existing),
                        map -> new ArrayList(map.values())
                ));

        return ResponseEntity
                .ok(filteredChatRooms);
    }
}
