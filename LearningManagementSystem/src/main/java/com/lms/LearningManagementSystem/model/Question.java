package com.lms.LearningManagementSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text;

    @NotBlank
    private String options;

    @NotBlank
    private String correctAnswer;


    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)  // foreign key column
    private Assignment assignment;
}
