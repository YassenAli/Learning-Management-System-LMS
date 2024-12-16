package com.lms.LearningManagementSystem.controller;

import com.lms.LearningManagementSystem.dto.NotificationRequest;
import com.lms.LearningManagementSystem.model.Notification;
import com.lms.LearningManagementSystem.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNotification(@RequestBody NotificationRequest notificationRequest) {
        notificationService.createNotification(notificationRequest.getUser(), notificationRequest.getSubject(), notificationRequest.getMessage(), notificationRequest.getEmail());
        return ResponseEntity.ok("Notification created successfully.");
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return notifications.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(notifications);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<Notification>> getAllNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getAllNotifications(userId);
        return notifications.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(notifications);
    }

    @PostMapping("/read/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        boolean isUpdated = notificationService.markAsRead(id);
        return isUpdated
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}