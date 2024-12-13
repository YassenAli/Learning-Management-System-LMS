package com.lms.LearningManagementSystem.service;

import com.lms.LearningManagementSystem.model.Course;
import com.lms.LearningManagementSystem.model.Lesson;
import com.lms.LearningManagementSystem.model.User;
import com.lms.LearningManagementSystem.repository.CourseRepository;
import com.lms.LearningManagementSystem.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course uploadMediaFiles(Long courseId, List<String> mediaFiles) {
        Course course = getCourseById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        course.getMediaFiles().addAll(mediaFiles);
        return courseRepository.save(course);
    }

    public Course enrollInCourse(Long courseId, User student) {
        Course course = getCourseById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        course.getEnrolledStudents().add(student);
        return courseRepository.save(course);
    }

    public List<User> getEnrolledStudents(Long courseId) {
        Course course = getCourseById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getEnrolledStudents();
    }

    public Lesson generateOtp(Long courseId, Long lessonId) {
        Course course = getCourseById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Lesson lesson = course.getLessons().stream()
                .filter(l -> l.getId().equals(lessonId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        String otp = String.valueOf((int) (Math.random() * 9000) + 1000);
        lesson.setOtp(otp);
        return lessonRepository.save(lesson);
    }

    public boolean validateOtp(Long courseId, Long lessonId, String otp) {
        Course course = getCourseById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        Lesson lesson = course.getLessons().stream()
                .filter(l -> l.getId().equals(lessonId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        return otp.equals(lesson.getOtp());
    }
}