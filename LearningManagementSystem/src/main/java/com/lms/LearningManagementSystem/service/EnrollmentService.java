package com.lms.LearningManagementSystem.service;

import com.lms.LearningManagementSystem.model.Enrollment;
import com.lms.LearningManagementSystem.model.User;
import com.lms.LearningManagementSystem.repository.CourseRepository;
import com.lms.LearningManagementSystem.repository.EnrollmentRepository;
import com.lms.LearningManagementSystem.repository.UserRepository;
import com.lms.LearningManagementSystem.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Admin can view all enrollments
    @PreAuthorize("hasRole('ADMIN')")
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    // Students can view their own enrollments, instructors can view enrollments for their courses
    @PreAuthorize("hasRole('STUDENT') and #studentId == authentication.principal.id or " +
                  "hasRole('INSTRUCTOR') and @courseSecurityService.isInstructorOfAnyEnrolledCourse(authentication.principal, #studentId) or " +
                  "hasRole('ADMIN')")
    public List<Enrollment> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    // Instructors can view enrollments for their courses, admin can view any
    @PreAuthorize("hasRole('INSTRUCTOR') and @courseSecurityService.isInstructorOfCourse(authentication.principal, #courseId) or " +
                  "hasRole('ADMIN')")
    public List<Enrollment> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    // Only students can enroll themselves in courses
    @PreAuthorize("hasRole('STUDENT') and #studentId == authentication.principal.id")
    public Enrollment enrollStudentInCourse(Long studentId, Long courseId) {
        if (enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }
        
        Enrollment enrollment = new Enrollment();
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }

    // Instructors can remove students from their courses, admin can remove from any course
    @PreAuthorize("hasRole('INSTRUCTOR') and @courseSecurityService.isInstructorOfCourse(authentication.principal, #courseId) or " +
                  "hasRole('ADMIN')")
    public void unenrollStudent(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);
    }
}
