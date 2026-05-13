package com.harun.assignmentmanager.DTO.Assignment;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveAssignmentRequestDTO {

    @NotNull
    private Long oldCourseId;
    @NotNull
    private Long newCourseId;
    @NotNull
    private Long assignmentId;
}
