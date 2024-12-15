package com.lms.LearningManagementSystem.service;

import com.lms.LearningManagementSystem.model.Assignment;
import com.lms.LearningManagementSystem.model.Question;
import com.lms.LearningManagementSystem.repository.AssignmentRepository;
import com.lms.LearningManagementSystem.model.Course;
import com.lms.LearningManagementSystem.repository.CourseRepository;
import com.lms.LearningManagementSystem.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    public Assignment saveAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Assignment gradeAssignment(Long id, Double grade, String feedback) {
        Assignment assignment = assignmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setGrade(grade);
        assignment.setFeedback(feedback);
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
}