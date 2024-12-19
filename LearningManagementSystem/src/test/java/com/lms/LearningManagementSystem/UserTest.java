package com.lms.LearningManagementSystem.controller;

import com.lms.LearningManagementSystem.model.User;
import com.lms.LearningManagementSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setEmail("test@example.com");

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(mockUser));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testUser"));
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(mockUser);

        mockMvc.perform(put("/api/users/{username}", "testUser")
                        .contentType("application/json")
                        .content("{\"username\": \"testUser\", \"email\": \"test@example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser_BadRequest() throws Exception {
        doThrow(new IllegalArgumentException("Invalid data")).when(userService).updateUser(anyString(), any(User.class));

        mockMvc.perform(put("/api/users/{username}", "testUser")
                        .contentType("application/json")
                        .content("{\"username\": \"testUser\", \"email\": \"invalid\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid data"));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("User not found")).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCurrentUserProfile() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(mockUser);

        mockMvc.perform(get("/api/users/profile").principal(authentication))
                .andExpect(status(
