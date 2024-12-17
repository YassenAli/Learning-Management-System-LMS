package com.lms.LearningManagementSystem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomErrorController.class)
public class CustomErrorControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testHandleError_404() throws Exception {
        // Simulate a 404 error scenario
        mockMvc.perform(get("/error")
                        .requestAttr("javax.servlet.error.status_code", 404))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"));
    }

    @Test
    void testHandleError_403() throws Exception {
        // Simulate a 403 error scenario
        mockMvc.perform(get("/error")
                        .requestAttr("javax.servlet.error.status_code", 403))
                .andExpect(status().isForbidden())
                .andExpect(view().name("error/403"));
    }

    @Test
    void testHandleError_GeneralError() throws Exception {
        // Simulate an unknown error scenario
        mockMvc.perform(get("/error")
                        .requestAttr("javax.servlet.error.status_code", 500))
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/error"));
    }
}
