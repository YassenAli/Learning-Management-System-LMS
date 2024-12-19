package java.com.lms.LearningManagementSystem;

import com.lms.LearningManagementSystem.model.Course;
import com.lms.LearningManagementSystem.model.User;
import com.lms.LearningManagementSystem.service.CourseService;
import com.lms.LearningManagementSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CourseController courseController;

    private Course course;
    private User instructor;

    @BeforeEach
    void setUp() {
        instructor = new User();
        instructor.setUsername("instructorUser");

        course = new Course();
        course.setId(1L);
        course.setTitle("Spring Boot Course");
        course.setDescription("A complete guide to Spring Boot");
        course.setInstructor(instructor);

        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void testGetAllCourses() throws Exception {
        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring Boot Course"))
                .andExpect(jsonPath("$[0].description").value("A complete guide to Spring Boot"));
    }

    @Test
    void testGetCourseById() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(get("/api/courses/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Boot Course"));
    }

    @Test
    void testCreateCourse_asInstructor() throws Exception {
        when(courseService.createCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Spring Boot Course\", \"description\": \"A comprehensive guide\", \"duration\": 10}")
                        .with(WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Boot Course"));
    }

    @Test
    void testUpdateCourse_asInstructor() throws Exception {
        when(courseService.updateCourse(1L, course)).thenReturn(course);

        mockMvc.perform(put("/api/courses/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Spring Boot Course\", \"description\": \"Updated description\", \"duration\": 12}")
                        .with(WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Spring Boot Course"));
    }

    @Test
    void testDeleteCourse_asInstructor() throws Exception {
        doNothing().when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/api/courses/{id}", 1L)
                        .with(WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})))
                .andExpect(status().isOk());
    }

    @Test
    void testEnrollInCourse_asStudent() throws Exception {
        doNothing().when(courseService).enrollStudent(1L, "studentUser");

        mockMvc.perform(post("/api/courses/{id}/enroll", 1L)
                        .with(WithMockUser(username = "studentUser", roles = {"STUDENT"})))
                .andExpect(status().isOk());
    }

    @Test
    void testUnenrollFromCourse_asStudent() throws Exception {
        doNothing().when(courseService).unenrollStudent(1L, "studentUser");

        mockMvc.perform(post("/api/courses/{id}/unenroll", 1L)
                        .with(WithMockUser(username = "studentUser", roles = {"STUDENT"})))
                .andExpect(status().isOk());
    }

    @Test
    void testGenerateOtp_asInstructor() throws Exception {
        when(courseService.generateOtp(1L, 1L)).thenReturn(new Lesson());

        mockMvc.perform(post("/api/courses/{courseId}/lessons/{lessonId}/generate-otp", 1L, 1L)
                        .with(WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})))
                .andExpect(status().isOk());
    }

    @Test
    void testValidateOtp_asStudent() throws Exception {
        when(courseService.validateOtp(1L, 1L, "123456")).thenReturn(true);

        mockMvc.perform(post("/api/courses/{courseId}/lessons/{lessonId}/validate-otp", 1L, 1L)
                        .content("123456")
                        .with(WithMockUser(username = "studentUser", roles = {"STUDENT"})))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testGetInstructorCourses() throws Exception {
        when(courseService.getCoursesByInstructor("instructorUser")).thenReturn(Arrays.asList(course));

        mockMvc.perform(get("/api/courses/instructor")
                        .with(WithMockUser(username = "instructorUser", roles = {"INSTRUCTOR"})))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Spring Boot Course"));
    }
}

