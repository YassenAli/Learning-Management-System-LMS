package com.lms.LearningManagementSystem.repository;
import com.lms.LearningManagementSystem.model.Course;
import com.lms.LearningManagementSystem.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
