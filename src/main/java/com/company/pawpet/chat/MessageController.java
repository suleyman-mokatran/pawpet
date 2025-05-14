package com.company.pawpet.chat;

import com.company.pawpet.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    AppUserService appUserService;

    @PostMapping("/send")
    public Message sendMessage(@RequestBody ChatMessage chatMessage) {
        Message message = new Message();
        message.setSenderId(chatMessage.getSenderId());
        message.setReceiverId(chatMessage.getReceiverId());
        message.setContent(chatMessage.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setPetId(chatMessage.getPetId());
        message.setType(chatMessage.getType());
        message.setSender(appUserService.getUserById(chatMessage.getSenderId().intValue()).orElseThrow());
        message.setReceiver(appUserService.getUserById(chatMessage.getReceiverId().intValue()).orElseThrow());

        return messageRepository.save(message);
    }

    @GetMapping("/history")
    public List<Message> getMessages(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
                senderId, receiverId, senderId, receiverId);
    }
}
