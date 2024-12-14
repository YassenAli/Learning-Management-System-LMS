package com.lms.LearningManagementSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String studentName;

    @NotBlank
    private String content;

    private Double grade;

    @NotBlank
    private String feedback;


}
