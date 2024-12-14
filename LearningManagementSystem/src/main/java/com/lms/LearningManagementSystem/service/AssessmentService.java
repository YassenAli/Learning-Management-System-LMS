package com.lms.LearningManagementSystem.service;

import com.lms.LearningManagementSystem.model.Assessment;
import com.lms.LearningManagementSystem.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question getRandomQuestion() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) throw new RuntimeException("No questions available");
        Random random = new Random();
        return questions.get(random.nextInt(questions.size()));
    }
}

@Service
class AssignmentService {

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