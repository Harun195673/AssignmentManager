package com.harun.assignmentmanager.DTO.Assignment;

import com.harun.assignmentmanager.Entity.Assignment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssignmentRequestDTO {

    @NotBlank
    private String title;

    @NotNull
    private LocalDate deadline;

    @NotNull
    private Assignment.Status status;

    @NotNull
    private Long courseId;
}