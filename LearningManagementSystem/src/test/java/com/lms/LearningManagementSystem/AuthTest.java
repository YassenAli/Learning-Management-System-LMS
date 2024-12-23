//
//package com.lms.LearningManagementSystem.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lms.LearningManagementSystem.model.User;
//import com.lms.LearningManagementSystem.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(AuthController.class)
//public class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private User testUser;
//
//    @BeforeEach
//    void setup() {
//        testUser = new User();
//        testUser.setUsername("john_doe");
//        testUser.setPassword("password123");
//        testUser.setRole(User.Role.STUDENT);
//    }
//
//    @Test
//    void testRegisterUserSuccess() throws Exception {
//        when(userService.createUser(any(User.class))).thenReturn(testUser);
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testUser)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("john_doe"))
//                .andExpect(jsonPath("$.password").doesNotExist());
//
//        verify(userService, times(1)).createUser(any(User.class));
//    }
//
//    @Test
//    void testRegisterUserBadRequest() throws Exception {
//        when(userService.createUser(any(User.class))).thenThrow(new IllegalArgumentException("Username already exists"));
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testUser)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error").value("Username already exists"));
//
//        verify(userService, times(1)).createUser(any(User.class));
//    }
//
//    @Test
//    void testLoginUserSuccess() throws Exception {
//        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
//        loginRequest.setUsername("john_doe");
//        loginRequest.setPassword("password123");
//
//        Authentication auth = mock(Authentication.class);
//        when(auth.getAuthorities()).thenReturn(Collections.emptyList());
//        when(authenticationManager.authenticate(any())).thenReturn(auth);
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("User logged in successfully"))
//                .andExpect(jsonPath("$.username").value("john_doe"));
//
//        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
//    }
//
//    @Test
//    void testLoginUserBadCredentials() throws Exception {
//        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
//        loginRequest.setUsername("john_doe");
//        loginRequest.setPassword("wrongpassword");
//
//        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid username or password"));
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.error").value("Invalid username or password"));
//
//        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
//    }
//
//    @Test
//    void testLogoutSuccess() throws Exception {
//        mockMvc.perform(post("/api/auth/logout"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Logged out successfully"));
//    }
//}
