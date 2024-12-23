package com.lms.LearningManagementSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.lms.LearningManagementSystem.model.Course;
import com.lms.LearningManagementSystem.service.CourseService;
import com.lms.LearningManagementSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest(classes = LearningManagementSystemApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @Mock
    private UserService userService;

    private String instructorToken;

    @BeforeEach
    void setUp() throws Exception {
        // Mock authentication and retrieve token
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "username": "testINSTRUCTOR",
                                "password": "password123"
                            }
                        """))
                .andExpect(status().isOk())
                .andReturn();

        instructorToken = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    @Test
    @WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})
    void createCourse() throws Exception {
        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("Test Description");
        course.setDuration(4);

        mockMvc.perform(post("/api/courses")
                        .header("Authorization", "Bearer " + instructorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "Test Course",
                                "description": "Test Description",
                                "duration": 4
                            }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})
    void updateCourse() throws Exception {
        mockMvc.perform(put("/api/courses/1")
                        .header("Authorization", "Bearer " + instructorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "Updated Title",
                                "description": "Updated Description",
                                "duration": 6
                            }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})
    void deleteCourse() throws Exception {
        mockMvc.perform(delete("/api/courses/1")
                        .header("Authorization", "Bearer " + instructorToken))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})
    void getInstructorCourses() throws Exception {
        mockMvc.perform(get("/api/courses/instructor")
                        .header("Authorization", "Bearer " + instructorToken))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})
    void uploadMediaFiles() throws Exception {
        mockMvc.perform(post("/api/courses/1/media")
                        .header("Authorization", "Bearer " + instructorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            ["file1.mp4", "file2.pdf"]
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})
    void generateOtp() throws Exception {
        mockMvc.perform(post("/api/courses/1/lessons/1/generate-otp")
                        .header("Authorization", "Bearer " + instructorToken))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})
    void getEnrolledStudents() throws Exception {
        mockMvc.perform(get("/api/courses/1/students")
                        .header("Authorization", "Bearer " + instructorToken))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})
    void addLessonsToCourse() throws Exception {
        mockMvc.perform(post("/api/courses/1/lessons")
                        .header("Authorization", "Bearer " + instructorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            [
                                {"title": "Lesson 1", "content": "Content 1"},
                                {"title": "Lesson 2", "content": "Content 2"}
                            ]
                        """))
                .andExpect(status().isOk());
    }





}
