package com.company.pawpet.chat;

import com.company.pawpet.Model.AppUser;
import com.company.pawpet.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "http://localhost:3000")
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

    @DeleteMapping("/deletemessage/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        messageRepository.deleteById(id);
        return ResponseEntity.ok("Message deleted successfully"); // 200 OK
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path imagePath = Paths.get("uploads", fileName);
        try {
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, file.getBytes());

            String imageUrl = "http://localhost:8080/messages/uploads/" + fileName;
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getreceiverinfo/{id}")
    public ResponseEntity<AppUser> getReceiverInfo(@PathVariable int id){
       return ResponseEntity.ok(appUserService.getUserById(id).orElseThrow());
    }

}
