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
@RequestMapping("/api/Assessment")

class AssessmentController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/questions")
    public Question addQuestion(@RequestBody Question question) {
        return questionService.saveQuestion(question);
    }

    @GetMapping("/questions/random")
    public Question getRandomQuestion() {
        return questionService.getRandomQuestion();
    }

    @PostMapping("/assignments")
    public Assignment submitAssignment(@RequestBody Assignment assignment) {
        return assignmentService.saveAssignment(assignment);
    }

    @PostMapping("/assignments/{id}/grade")
    public Assignment gradeAssignment(@PathVariable Long id, @RequestParam Double grade, @RequestParam String feedback) {
        return assignmentService.gradeAssignment(id, grade, feedback);
    }

    @GetMapping("/assignments")
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

}
