package com.company.pawpet.chat;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Service.AppUserService;
import com.company.pawpet.Service.NotificationService;
import com.company.pawpet.notification.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class ChatWebSocketController {

    @Autowired
    MessageRepository messageRepository;

    private final NotificationHandler notificationHandler;

    public ChatWebSocketController(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }
    @Autowired
    NotificationService notificationService;

    @Autowired
    AppUserService appUserService;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message handleChatMessage(@Payload ChatMessage chatMessage) throws IOException {
        Message message = new Message();
        message.setSenderId(chatMessage.getSenderId());
        message.setReceiverId(chatMessage.getReceiverId());
        message.setContent(chatMessage.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setPetId(chatMessage.getPetId());
        message.setType(chatMessage.getType());
        AppUser sender = appUserService.getUserById(chatMessage.getSenderId().intValue()).orElseThrow();
        message.setSender(sender);
        AppUser receiver = appUserService.getUserById(chatMessage.getReceiverId().intValue()).orElseThrow();
        message.setReceiver(receiver);
        String name = sender.getFirstname() + " " + receiver.getLastname();
        notificationService.addNewMessageNotification(chatMessage.getReceiverId().intValue(),"New message from "+ message.getSender().getFirstname() + " " + message.getSender().getLastname());
        Message saved = messageRepository.save(message);
        notificationHandler.sendNotificationToUser(chatMessage.getReceiverId().intValue(), "New message from "+ message.getSender().getFirstname() + " " + message.getSender().getLastname());

        return saved;
    }

}