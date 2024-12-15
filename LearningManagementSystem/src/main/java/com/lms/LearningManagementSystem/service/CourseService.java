package com.lms.LearningManagementSystem.service;

import com.lms.LearningManagementSystem.model.Course;
import com.lms.LearningManagementSystem.model.Lesson;
import com.lms.LearningManagementSystem.model.User;
import com.lms.LearningManagementSystem.service.UserService;
import com.lms.LearningManagementSystem.repository.CourseRepository;
import com.lms.LearningManagementSystem.repository.LessonRepository;
import com.lms.LearningManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public Course createCourse(Course course) {

        // Get the instructor from the database
        User instructor = userRepository.findByUsername(course.getInstructor().getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') and @courseSecurityService.isInstructorOfCourse(authentication.principal.username, #courseId) or hasRole('ADMIN')")
    public Course updateCourse(Long courseId, Course course) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // Preserve the original instructor
        course.setId(courseId);
        course.setInstructor(existingCourse.getInstructor());
        course.setEnrolledStudents(existingCourse.getEnrolledStudents());

        return courseRepository.save(course);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') and @courseSecurityService.isInstructorOfCourse(authentication.principal.username, #courseId) or hasRole('ADMIN')")
    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new IllegalArgumentException("Course not found");
        }
        courseRepository.deleteById(courseId);
    }

    // Accessible by all authenticated users
    @PreAuthorize("isAuthenticated()")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<Course> getCoursesByInstructor(String instructorUsername) {
        return courseRepository.findByInstructorUsername(instructorUsername);
    }

    @PreAuthorize("hasRole('STUDENT')")
    public List<Course> getEnrolledCourses(String username) {
        return courseRepository.findByEnrolledStudentsContaining(username);
    }

    @PreAuthorize("hasRole('STUDENT')")
    public void enrollStudent(Long courseId, String username) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (course.getEnrolledStudents().contains(username)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

        course.getEnrolledStudents().add(username);
        courseRepository.save(course);
    }

    @PreAuthorize("hasRole('STUDENT')")
    public void unEnrollStudent(Long courseId, String username) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!course.getEnrolledStudents().contains(username)) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        course.getEnrolledStudents().remove(username);
        courseRepository.save(course);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') and @courseSecurityService.isInstructorOfCourse(authentication.principal.username, #courseId)")
    public Course uploadMediaFiles(Long courseId, List<String> mediaFiles) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        course.getMediaFiles().addAll(mediaFiles);
        return courseRepository.save(course);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') and @courseSecurityService.isInstructorOfCourse(authentication.principal.username, #courseId)")
    public Lesson generateOtp(Long courseId, Long lessonId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Lesson lesson = course.getLessons().stream()
                .filter(l -> l.getId().equals(lessonId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        // Generate a random 6-digit OTP
        String otp = String.format("%06d", (int)(Math.random() * 1000000));
        lesson.setOtp(otp);

        courseRepository.save(course);
        return lesson;
    }

    @PreAuthorize("hasRole('STUDENT') and @courseSecurityService.isEnrolledInCourse(authentication.principal.username, #courseId)")
    public boolean validateOtp(Long courseId, Long lessonId, String otp) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Lesson lesson = course.getLessons().stream()
                .filter(l -> l.getId().equals(lessonId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        return otp.equals(lesson.getOtp());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR') and @courseSecurityService.isInstructorOfCourse(authentication.principal, #courseId)")
    public List<String> getEnrolledStudents(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return course.getEnrolledStudents().stream().collect(Collectors.toList());
    }
}