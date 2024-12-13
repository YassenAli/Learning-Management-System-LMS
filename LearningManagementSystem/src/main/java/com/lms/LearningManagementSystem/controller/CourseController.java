package com.lms.LearningManagementSystem.controller;

import com.lms.LearningManagementSystem.model.Course;
import com.lms.LearningManagementSystem.model.Lesson;
import com.lms.LearningManagementSystem.model.User;
import com.lms.LearningManagementSystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Optional<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping("/{id}/media")
    public Course uploadMediaFiles(@PathVariable Long id, @RequestBody List<String> mediaFiles) {
        return courseService.uploadMediaFiles(id, mediaFiles);
    }

    @PostMapping("/{id}/enroll")
    public Course enrollInCourse(@PathVariable Long id, @RequestBody User student) {
        return courseService.enrollInCourse(id, student);
    }

    @GetMapping("/{id}/students")
    public List<User> getEnrolledStudents(@PathVariable Long id) {
        return courseService.getEnrolledStudents(id);
    }

    @PostMapping("/{courseId}/lessons/{lessonId}/otp")
    public Lesson generateOtp(@PathVariable Long courseId, @PathVariable Long lessonId) {
        return courseService.generateOtp(courseId, lessonId);
    }

    @PostMapping("/{courseId}/lessons/{lessonId}/validate")
    public boolean validateOtp(@PathVariable Long courseId, @PathVariable Long lessonId, @RequestBody String otp) {
        return courseService.validateOtp(courseId, lessonId, otp);
    }
}