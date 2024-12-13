package com.lms.LearningManagementSystem.service;

import com.lms.LearningManagementSystem.model.Notification;
import com.lms.LearningManagementSystem.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmailNotificationService emailNotificationService;

    public NotificationService(NotificationRepository notificationRepository, EmailNotificationService emailNotificationService) {
        this.notificationRepository = notificationRepository;
        this.emailNotificationService = emailNotificationService;
    }

    public void createNotification(Long userId, String message, String email) {
        // Save notification in database
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setIsRead(false);
        notificationRepository.save(notification);

        // Send email notification
        emailNotificationService.sendEmail(email, "New Notification", message);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    public List<Notification> getAllNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
}
