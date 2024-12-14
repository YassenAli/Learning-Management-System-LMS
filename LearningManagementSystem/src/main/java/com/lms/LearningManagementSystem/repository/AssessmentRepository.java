package com.lms.LearningManagementSystem.repository;

import com.lms.LearningManagementSystem.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



public interface QuestionRepository extends JpaRepository<Question, Long> {}

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {}
