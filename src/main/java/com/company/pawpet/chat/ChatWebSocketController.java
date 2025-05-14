package com.company.pawpet.chat;

import com.company.pawpet.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatWebSocketController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AppUserService appUserService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message handleChatMessage(@Payload ChatMessage chatMessage) {
        Message message = new Message();
        message.setSenderId(chatMessage.getSenderId());
        message.setReceiverId(chatMessage.getReceiverId());
        message.setContent(chatMessage.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setPetId(chatMessage.getPetId());
        message.setType(chatMessage.getType());
        message.setSender(appUserService.getUserById(chatMessage.getSenderId().intValue()).orElseThrow());
        message.setReceiver(appUserService.getUserById(chatMessage.getReceiverId().intValue()).orElseThrow());

        Message saved = messageRepository.save(message);
        return saved;
    }

}