// Test Cases
package com.lms.LearningManagementSystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssessmentModuleApplicationTests {

    private final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort
    private int port;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AssignmentService assignmentService;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @Test
    void testAddQuestion() {
        Question question = new Question();
        question.setText("What is Java?");
        question.setOptions("A. Language, B. Island, C. Coffee");
        question.setCorrectAnswer("A");

        ResponseEntity<Question> response = restTemplate.postForEntity(getBaseUrl() + "/questions", question, Question.class);
        assertNotNull(response.getBody());
        assertEquals("What is Java?", response.getBody().getText());
    }

    @Test
    void testRandomQuestion() {
        Question question = new Question();
        question.setText("What is Spring Boot?");
        question.setOptions("A. Framework, B. Tool");
        question.setCorrectAnswer("A");
        questionService.saveQuestion(question);

        Question randomQuestion = restTemplate.getForObject(getBaseUrl() + "/questions/random", Question.class);
        assertNotNull(randomQuestion);
    }

    @Test
    void testSubmitAssignment() {
        Assignment assignment = new Assignment();
        assignment.setTitle("Math Assignment");
        assignment.setStudentName("John Doe");
        assignment.setContent("Solve 10 equations");

        ResponseEntity<Assignment> response = restTemplate.postForEntity(getBaseUrl() + "/assignments", assignment, Assignment.class);
        assertNotNull(response.getBody());
        assertEquals("Math Assignment", response.getBody().getTitle());
    }

    @Test
    void testGradeAssignment() {
        Assignment assignment = new Assignment();
        assignment.setTitle("Math Assignment");
        assignment.setStudentName("John Doe");
        assignment.setContent("Solve 10 equations");
        assignment = assignmentService.saveAssignment(assignment);

        restTemplate.postForEntity(getBaseUrl() + "/assignments/" + assignment.getId() + "/grade?grade=90&feedback=Good work", null, Assignment.class);
        Assignment gradedAssignment = assignmentService.gradeAssignment(assignment.getId(), 90.0, "Good work");

        assertEquals(90, gradedAssignment.getGrade());
        assertEquals("Good work", gradedAssignment.getFeedback());
    }

    @Test
    void testGetAllAssignments() {
        Assignment assignment = new Assignment();
        assignment.setTitle("Math Assignment");
        assignment.setStudentName("John Doe");
        assignment.setContent("Solve 10 equations");
        assignmentService.saveAssignment(assignment);

        Assignment[] assignments = restTemplate.getForObject(getBaseUrl() + "/assignments", Assignment[].class);
        assertNotNull(assignments);
        assertTrue(assignments.length > 0);
    }
}
