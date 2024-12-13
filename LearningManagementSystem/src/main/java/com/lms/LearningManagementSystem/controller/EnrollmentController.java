package com.lms.LearningManagementSystem.controller;

import com.lms.LearningManagementSystem.model.Enrollment;
import com.lms.LearningManagementSystem.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // Get all enrollments
    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    // Get enrollments for a specific student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    // Get enrollments for a specific course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseId(courseId));
    }

    // Enroll a student in a course
    @PostMapping
    public ResponseEntity<Enrollment> enrollStudentInCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        return ResponseEntity.ok(enrollmentService.enrollStudentInCourse(studentId, courseId));
    }

    // Unenroll a student from a course
    @DeleteMapping
    public ResponseEntity<Void> unenrollStudent(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        enrollmentService.unenrollStudent(studentId, courseId);
        return ResponseEntity.noContent().build();
    }
}
