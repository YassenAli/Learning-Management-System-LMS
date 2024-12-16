package com.lms.LearningManagementSystem.dto;

import com.lms.LearningManagementSystem.model.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationRequest {
    // Getters and setters
    private User user;
    private String subject;
    private String message;

}