//package com.lms.LearningManagementSystem;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lms.LearningManagementSystem.controller.AssessmentController;
//import com.lms.LearningManagementSystem.model.*;
//import com.lms.LearningManagementSystem.service.AssignmentService;
//import com.lms.LearningManagementSystem.service.QuestionService;
//import com.lms.LearningManagementSystem.service.QuizService;
//import com.lms.LearningManagementSystem.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.http.MediaType;
//
//import java.util.Arrays;
//import java.util.List;
//
//@ExtendWith(MockitoExtension.class)
//class AssessmentTest {
//
//    @InjectMocks
//    private AssessmentController assessmentController;
//
//    @Mock
//    private QuestionService questionService;
//
//    @Mock
//    private QuizService quizService;
//
//    @Mock
//    private AssignmentService assignmentService;
//
//    private MockMvc mockMvc;
//
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(assessmentController).build();
//        objectMapper = new ObjectMapper();
//    }
//
//    // Test for adding a question
//    @Test
//    void addQuestion() throws Exception {
//        Question question = new Question();
//        question.setText("What is Java?");
//        question.setOptions("A. Language, B. Platform, C. Library, D. None");
//        question.setCorrectAnswer("A");
//
//        when(questionService.saveQuestion(any(Question.class))).thenReturn(question);
//
//        mockMvc.perform(post("/api/Assessment/questions")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(question)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.text").value("What is Java?"))
//                .andExpect(jsonPath("$.options").value("A. Language, B. Platform, C. Library, D. None"))
//                .andExpect(jsonPath("$.correctAnswer").value("A"));
//
//        verify(questionService, times(1)).saveQuestion(any(Question.class));
//    }
//
//    // Test for getting a random question
//    @Test
//    void getRandomQuestion() throws Exception {
//        Question question = new Question();
//        question.setText("What is C++?");
//        question.setOptions("A. Language, B. Platform, C. Library, D. None");
//        question.setCorrectAnswer("A");
//
//        when(questionService.getRandomQuestion()).thenReturn(question);
//
//        mockMvc.perform(get("/api/Assessment/questions/random"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.text").value("What is C++?"))
//                .andExpect(jsonPath("$.options").value("A. Language, B. Platform, C. Library, D. None"))
//                .andExpect(jsonPath("$.correctAnswer").value("A"));
//
//        verify(questionService, times(1)).getRandomQuestion();
//    }
//
//    // Test for submitting an assignment
//    @Test
//    void submitAssignment() throws Exception {
//        Assignment assignment = new Assignment();
//        assignment.setTitle("Assignment 1");
//        assignment.setStudentName("Muhammad Fathi");
//        assignment.setContent("Any Content");
//        assignment.setFeedback("Great work!");
//        assignment.setGrade(90.0);
//
//        when(assignmentService.saveAssignment(any(Assignment.class))).thenReturn(assignment);
//
//        mockMvc.perform(post("/api/Assessment/assignments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(assignment)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("Assignment 1"))
//                .andExpect(jsonPath("$.studentName").value("Muhammad Fathi"))
//                .andExpect(jsonPath("$.content").value("Any Content"))
//                .andExpect(jsonPath("$.grade").value(90.0))
//                .andExpect(jsonPath("$.feedback").value("Great work!"));
//
//        verify(assignmentService, times(1)).saveAssignment(any(Assignment.class));
//    }
//
//    // Test for grading an assignment
//    @Test
//    void gradeAssignment() throws Exception {
//        Assignment assignment = new Assignment();
//        assignment.setId(1L);
//        assignment.setTitle("Assignment 1");
//        assignment.setStudentName("Muhammad Fathi");
//        assignment.setContent("Any Content");
//        assignment.setFeedback("Good job!");
//        assignment.setGrade(95.0);
//
//        when(assignmentService.gradeAssignment(eq(1L), anyDouble(), anyString())).thenReturn(assignment);
//
//        mockMvc.perform(post("/api/Assessment/assignments/1/grade")
//                        .param("grade", "95.0")
//                        .param("feedback", "Good job!"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.grade").value(95.0))
//                .andExpect(jsonPath("$.feedback").value("Good job!"));
//
//        verify(assignmentService, times(1)).gradeAssignment(eq(1L), anyDouble(), anyString());
//    }
//
//    // Test for getting all assignments
//    @Test
//    void getAllAssignments() throws Exception {
//        Assignment assignment1 = new Assignment();
//        assignment1.setId(1L);
//        assignment1.setTitle("Assignment 1");
//        assignment1.setStudentName("Muhammad Fathi");
//        assignment1.setContent("Content 1");
//        assignment1.setGrade(90.0);
//        assignment1.setFeedback("Well done");
//
//        Assignment assignment2 = new Assignment();
//        assignment2.setId(2L);
//        assignment2.setTitle("Assignment 2");
//        assignment2.setStudentName("Abdo Ali");
//        assignment2.setContent("Content 2");
//        assignment2.setGrade(85.0);
//        assignment2.setFeedback("Needs improvement");
//
//        List<Assignment> assignments = Arrays.asList(assignment1, assignment2);
//        when(assignmentService.getAllAssignments()).thenReturn(assignments);
//
//        mockMvc.perform(get("/api/Assessment/assignments"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[1].id").value(2));
//
//        verify(assignmentService, times(1)).getAllAssignments();
//    }
//
//    @Test
//    void submitAssignmentWithCourse() throws Exception {
//
//        Course course = new Course();
//        course.setTitle("English Course");
//        course.setDescription("Description of English Course");
//
//        Assignment assignment = new Assignment();
//        assignment.setTitle("Assignment 1");
//        assignment.setStudentName("Mohamed Ahmed");
//        assignment.setContent("Content of the assignment.");
//        assignment.setFeedback("Great work!");
//        assignment.setGrade(90.0);
//        assignment.setCourse(course);
//
//        when(assignmentService.saveAssignment(any(Assignment.class))).thenReturn(assignment);
//
//        mockMvc.perform(post("/api/Assessment/assignments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(assignment)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.course.title").value("English Course"));
//
//        verify(assignmentService, times(1)).saveAssignment(any(Assignment.class));
//    }
//
//    @Test
//    void addQuiz() throws Exception {
//        Quiz quiz = new Quiz();
//        quiz.setTitle("Java Basics Quiz");
//        quiz.setDescription("A quiz about basic Java concepts.");
//        quiz.setTotalMarks(100l);
//
//        when(quizService.saveQuiz(any(Quiz.class))).thenReturn(quiz);
//
//        mockMvc.perform(post("/api/Assessment/quizzes")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(quiz)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("Java Basics Quiz"))
//                .andExpect(jsonPath("$.description").value("A quiz about basic Java concepts."))
//                .andExpect(jsonPath("$.totalMarks").value(100));
//
//        verify(quizService, times(1)).saveQuiz(any(Quiz.class));
//    }
//
//    // Test for getting a quiz by ID
//    @Test
//    void getQuizById() throws Exception {
//        Quiz quiz = new Quiz();
//        quiz.setId(1L);
//        quiz.setTitle("Java Basics Quiz");
//        quiz.setDescription("A quiz about basic Java concepts.");
//        quiz.setTotalMarks(100l);
//
//        when(quizService.getQuiz(1L)).thenReturn(quiz);
//
//        mockMvc.perform(get("/api/Assessment/quizzes/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.title").value("Java Basics Quiz"))
//                .andExpect(jsonPath("$.description").value("A quiz about basic Java concepts."))
//                .andExpect(jsonPath("$.totalMarks").value(100));
//
//        verify(quizService, times(1)).getQuiz(1L);
//    }
//
//    // Test for getting all quizzes
//    @Test
//    void getAllQuizzes() throws Exception {
//        Quiz quiz1 = new Quiz();
//        quiz1.setId(1L);
//        quiz1.setTitle("Java Basics Quiz");
//        quiz1.setDescription("A quiz about basic Java concepts.");
//        quiz1.setTotalMarks(100l);
//
//        Quiz quiz2 = new Quiz();
//        quiz2.setId(2L);
//        quiz2.setTitle("Advanced Java Quiz");
//        quiz2.setDescription("A quiz about advanced Java concepts.");
//        quiz2.setTotalMarks(100l);
//
//        List<Quiz> quizzes = Arrays.asList(quiz1, quiz2);
//        when(quizService.getAllQuizzes()).thenReturn(quizzes);
//
//        mockMvc.perform(get("/api/Assessment/quizzes"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[1].id").value(2));
//
//        verify(quizService, times(1)).getAllQuizzes();
//    }
//
//    // Test for updating a quiz
//    @Test
//    void updateQuiz() throws Exception {
//        Quiz quiz = new Quiz();
//        quiz.setId(1L);
//        quiz.setTitle("Updated Java Basics Quiz");
//        quiz.setDescription("An updated quiz about basic Java concepts.");
//        quiz.setTotalMarks(150l);
//
//        when(quizService.updateQuiz(eq(1L), any(Quiz.class))).thenReturn(quiz);
//
//        mockMvc.perform(put("/api/Assessment/quizzes/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(quiz)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.title").value("Updated Java Basics Quiz"))
//                .andExpect(jsonPath("$.description").value("An updated quiz about basic Java concepts."))
//                .andExpect(jsonPath("$.totalMarks").value(150));
//
//        verify(quizService, times(1)).updateQuiz(eq(1L), any(Quiz.class));
//    }
//}
