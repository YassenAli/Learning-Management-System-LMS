package com.lms.LearningManagementSystem;

import com.lms.LearningManagementSystem.model.Course;
import com.lms.LearningManagementSystem.repository.CourseRepository;
import com.lms.LearningManagementSystem.service.CourseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.boot.test.mock.mockito.SpringMockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CourseServiceTest {

//    @SpringMockBean
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Value("${media.storage.path}")
    private String mediaStoragePath;

    @Test
    public void testUploadMediaFiles_Success() throws Exception {
        // Mock data
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setMediaFiles(new ArrayList<>());

        MultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "Dummy content".getBytes()
        );

        List<MultipartFile> files = List.of(file);

        // Mock repository behavior
        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        Mockito.when(courseRepository.save(Mockito.any(Course.class))).thenAnswer(i -> i.getArgument(0));

        // Run the service method
        Course updatedCourse = courseService.uploadMediaFiles(courseId, files);

        // Assertions
        assertNotNull(updatedCourse);
        assertEquals(1, updatedCourse.getMediaFiles().size());
        assertTrue(updatedCourse.getMediaFiles().get(0).contains("test.pdf"));

        // Verify repository interactions
        Mockito.verify(courseRepository, Mockito.times(1)).findById(courseId);
        Mockito.verify(courseRepository, Mockito.times(1)).save(course);
    }

    @Test
    public void testUploadMediaFiles_CourseNotFound() {
        // Mock data
        Long courseId = 1L;
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "Dummy content".getBytes()
        );

        List<MultipartFile> files = List.of(file);

        // Mock repository behavior
        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Run the service method and assert exception
        assertThrows(IllegalArgumentException.class, () -> courseService.uploadMediaFiles(courseId, files));
    }
}
