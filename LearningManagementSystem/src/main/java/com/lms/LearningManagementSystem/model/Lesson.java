package com.lms.LearningManagementSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String otp; // OTP for attendance

    @ManyToOne
    @JoinColumn(name = "course_id") // Reference to the course this lesson belongs to
    private Course course;
}
