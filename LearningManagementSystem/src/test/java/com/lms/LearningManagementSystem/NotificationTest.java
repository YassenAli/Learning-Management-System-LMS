package com.lms.LearningManagementSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.LearningManagementSystem.dto.NotificationRequest;
import com.lms.LearningManagementSystem.model.Notification;
import com.lms.LearningManagementSystem.model.User;
import com.lms.LearningManagementSystem.service.NotificationService;
import com.lms.LearningManagementSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private NotificationRequest notificationRequest;
    private User testUser;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("john_doe");

        notificationRequest = new NotificationRequest();
        notificationRequest.setUserId(1L);
        notificationRequest.setSubject("Test Notification");
        notificationRequest.setMessage("This is a test notification.");

        testNotification = new Notification();
        testNotification.setId(1L);
        testNotification.setSubject("Test Notification");
        testNotification.setMessage("This is a test notification.");
        testNotification.setRead(false);
    }

    @Test
    void testCreateNotificationSuccess() throws Exception {
        when(userService.findById(1L)).thenReturn(testUser);
        doNothing().when(notificationService).createNotification(any(User.class), anyString(), anyString());

        mockMvc.perform(post("/notifications/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification created successfully."));

        verify(userService, times(1)).findById(1L);
        verify(notificationService, times(1)).createNotification(any(User.class), anyString(), anyString());
    }

    @Test
    void testCreateNotificationUserNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(post("/notifications/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found."));

        verify(userService, times(1)).findById(1L);
        verify(notificationService, never()).createNotification(any(User.class), anyString(), anyString());
    }

    @Test
    void testGetUnreadNotificationsSuccess() throws Exception {
        List<Notification> unreadNotifications = Arrays.asList(testNotification);
        when(notificationService.getUnreadNotifications(1L)).thenReturn(unreadNotifications);

        mockMvc.perform(get("/notifications/unread/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].subject").value("Test Notification"))
                .andExpect(jsonPath("$[0].read").value(false));

        verify(notificationService, times(1)).getUnreadNotifications(1L);
    }

    @Test
    void testGetUnreadNotificationsNoContent() throws Exception {
        when(notificationService.getUnreadNotifications(1L)).thenReturn(List.of());

        mockMvc.perform(get("/notifications/unread/1"))
                .andExpect(status().isNoContent());

        verify(notificationService, times(1)).getUnreadNotifications(1L);
    }

    @Test
    void testGetAllNotificationsSuccess() throws Exception {
        List<Notification> allNotifications = Arrays.asList(testNotification);
        when(notificationService.getAllNotifications(1L)).thenReturn(allNotifications);

        mockMvc.perform(get("/notifications/all/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].subject").value("Test Notification"));

        verify(notificationService, times(1)).getAllNotifications(1L);
    }

    @Test
    void testGetAllNotificationsNoContent() throws Exception {
        when(notificationService.getAllNotifications(1L)).thenReturn(List.of());

        mockMvc.perform(get("/notifications/all/1"))
                .andExpect(status().isNoContent());

        verify(notificationService, times(1)).getAllNotifications(1L);
    }

    @Test
    void testMarkAsReadSuccess() throws Exception {
        when(notificationService.markAsRead(1L)).thenReturn(true);

        mockMvc.perform(post("/notifications/read/1"))
                .andExpect(status().isNoContent());

        verify(notificationService, times(1)).markAsRead(1L);
    }

    @Test
    void testMarkAsReadNotFound() throws Exception {
        when(notificationService.markAsRead(1L)).thenReturn(false);

        mockMvc.perform(post("/notifications/read/1"))
                .andExpect(status().isNotFound());

        verify(notificationService, times(1)).markAsRead(1L);
    }
}

