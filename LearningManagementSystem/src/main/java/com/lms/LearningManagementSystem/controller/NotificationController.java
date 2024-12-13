package com.lms.LearningManagementSystem.controller;

import com.lms.LearningManagementSystem.model.Notification;
import com.lms.LearningManagementSystem.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread")
    public List<Notification> getUnreadNotifications(@RequestParam Long userId) {
        return notificationService.getUnreadNotifications(userId);
    }

    @GetMapping("/all")
    public List<Notification> getAllNotifications(@RequestParam Long userId) {
        return notificationService.getAllNotifications(userId);
    }

    @PostMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
    }
}

