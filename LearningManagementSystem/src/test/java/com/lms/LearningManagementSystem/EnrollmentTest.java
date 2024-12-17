package com.lms.LearningManagementSystem.controller;

import com.lms.LearningManagementSystem.model.Enrollment;
import com.lms.LearningManagementSystem.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
public class EnrollmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private EnrollmentController enrollmentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();
    }

    @Test
    void testGetAllEnrollments_AdminRole() throws Exception {
        Enrollment enrollment = new Enrollment();
        List<Enrollment> enrollments = Collections.singletonList(enrollment);
        when(enrollmentService.getAllEnrollments()).thenReturn(enrollments);

        mockMvc.perform(get("/api/enrollments")
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());

        verify(enrollmentService, times(1)).getAllEnrollments();
    }

    @Test
    void testGetEnrollmentsByStudentId_StudentRole() throws Exception {
        Long studentId = 1L;
        Enrollment enrollment = new Enrollment();
        List<Enrollment> enrollments = Collections.singletonList(enrollment);
        when(enrollmentService.getEnrollmentsByStudentId(studentId)).thenReturn(enrollments);

        mockMvc.perform(get("/api/enrollments/student/{studentId}", studentId)
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());

        verify(enrollmentService, times(1)).getEnrollmentsByStudentId(studentId);
    }

    @Test
    void testGetEnrollmentsByCourseId_InstructorRole() throws Exception {
        Long courseId = 1L;
        Enrollment enrollment = new Enrollment();
        List<Enrollment> enrollments = Collections.singletonList(enrollment);
        when(enrollmentService.getEnrollmentsByCourseId(courseId)).thenReturn(enrollments);

        mockMvc.perform(get("/api/enrollments/course/{courseId}", courseId)
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());

        verify(enrollmentService, times(1)).getEnrollmentsByCourseId(courseId);
    }

    @Test
    void testEnrollStudentInCourse_StudentRole() throws Exception {
        Long studentId = 1L;
        Long courseId = 1L;
        Enrollment enrollment = new Enrollment();
        when(enrollmentService.enrollStudentInCourse(studentId, courseId)).thenReturn(enrollment);

        mockMvc.perform(post("/api/enrollments/enroll")
                        .param("courseId", String.valueOf(courseId))
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(enrollmentService, times(1)).enrollStudentInCourse(studentId, courseId);
    }

    @Test
    void testUnenrollStudent_InstructorRole() throws Exception {
        Long studentId = 1L;
        Long courseId = 1L;

        mockMvc.perform(delete("/api/enrollments/unenroll")
                        .param("studentId", String.valueOf(studentId))
                        .param("courseId", String.valueOf(courseId))
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk());

        verify(enrollmentService, times(1)).unenrollStudent(studentId, courseId);
    }

    @Test
    void testEnrollStudentInCourse_BadRequest() throws Exception {
        Long studentId = 1L;
        Long courseId = 1L;
        when(enrollmentService.enrollStudentInCourse(studentId, courseId)).thenThrow(new IllegalArgumentException("Course not found"));

        mockMvc.perform(post("/api/enrollments/enroll")
                        .param("courseId", String.valueOf(courseId))
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Course not found"));

        verify(enrollmentService, times(1)).enrollStudentInCourse(studentId, courseId);
    }

    @Test
    void testUnenrollStudent_BadRequest() throws Exception {
        Long studentId = 1L;
        Long courseId = 1L;
        when(enrollmentService.unenrollStudent(studentId, courseId)).thenThrow(new IllegalArgumentException("Enrollment not found"));

        mockMvc.perform(delete("/api/enrollments/unenroll")
                        .param("studentId", String.valueOf(studentId))
                        .param("courseId", String.valueOf(courseId))
                        .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Enrollment not found"));

        verify(enrollmentService, times(1)).unenrollStudent(studentId, courseId);
    }
}

