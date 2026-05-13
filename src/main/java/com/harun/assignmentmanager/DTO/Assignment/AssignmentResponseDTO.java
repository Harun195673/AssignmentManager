package com.harun.assignmentmanager.DTO.Assignment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class AssignmentResponseDTO {

    private Long id;
    private String title;
    private LocalDate deadline;
    private String status;
    private Long courseId;
    private String courseName; // useful for frontend
}