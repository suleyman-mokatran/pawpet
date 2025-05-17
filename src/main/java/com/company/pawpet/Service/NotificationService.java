package com.company.pawpet.Service;

import com.company.pawpet.Model.Notification;
import com.company.pawpet.Repository.NotificationRepository;
import com.company.pawpet.chat.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    AppUserService appUserService;

    public List<Notification> getAllNotificationsForUser(int userId) {
        return notificationRepository.findNotificationsByUserId(userId);
    }

    public int getUnreadCount(int userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    public void markAsRead(int notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public Notification addNewMessageNotification(int id, String message){
        Notification newNotification = new Notification();
        newNotification.setRead(false);
        newNotification.setAppUser(appUserService.getUserById(id).orElseThrow());
        newNotification.setType("MESSAGE");
        newNotification.setTitle(message);
        newNotification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(newNotification);
    }

    public Notification addNewNotification(int id, String message){
        Notification newNotification = new Notification();
        newNotification.setRead(false);
        newNotification.setAppUser(appUserService.getUserById(id).orElseThrow());
        newNotification.setType("ALERT");
        newNotification.setTitle(message);
        newNotification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(newNotification);
    }

    public void deleteNotification(int id){
        notificationRepository.deleteById(id);
    }
}

