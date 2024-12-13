// Course.java
package com.lms.LearningManagementSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private Integer duration; // Duration in weeks or days

    @ElementCollection
    private List<String> mediaFiles = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToMany(mappedBy = "enrolledCourses")
    private List<User> students = new ArrayList<>(); // List of users enrolled as students

    @ManyToOne
    @JoinColumn(name = "instructor_id") // The instructor who owns this course
    private User instructor;

    // Add the getEnrolledStudents method
    public List<User> getEnrolledStudents() {
        return students;
    }
}