package com.lms.LearningManagementSystem.controller;

import com.lms.LearningManagementSystem.model.Course;
import com.lms.LearningManagementSystem.model.Lesson;
import com.lms.LearningManagementSystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Get all courses
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // Get course by ID
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return ResponseEntity.of(courseService.getCourseById(id));
    }

    // Create a new course
    @PostMapping
    public Course createCourse(@Validated @RequestBody Course course) {
        return courseService.createCourse(course);
    }

    // Upload media files to a course
    @PostMapping("/{courseId}/media")
    public ResponseEntity<Course> uploadMediaFiles(@PathVariable Long courseId, @RequestBody List<String> mediaFiles) {
        return ResponseEntity.ok(courseService.uploadMediaFiles(courseId, mediaFiles));
    }

    // Enroll a student in a course
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<Course> enrollInCourse(@PathVariable Long courseId, @RequestParam String studentName) {
        return ResponseEntity.ok(courseService.enrollInCourse(courseId, studentName));
    }

    // Get list of enrolled students
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<String>> getEnrolledStudents(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getEnrolledStudents(courseId));
    }

    // Generate OTP for a lesson
    @PostMapping("/{courseId}/lessons/{lessonId}/generate-otp")
    public ResponseEntity<Lesson> generateOtp(@PathVariable Long courseId, @PathVariable Long lessonId) {
        return ResponseEntity.ok(courseService.generateOtp(courseId, lessonId));
    }

    // Validate OTP for attendance
    @PostMapping("/{courseId}/lessons/{lessonId}/validate-otp")
    public ResponseEntity<Boolean> validateOtp(@PathVariable Long courseId, @PathVariable Long lessonId, @RequestBody String otp) {
        return ResponseEntity.ok(courseService.validateOtp(courseId, lessonId, otp));
    }
}

