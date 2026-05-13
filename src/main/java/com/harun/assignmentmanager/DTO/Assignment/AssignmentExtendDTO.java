package com.harun.assignmentmanager.DTO.Assignment;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentExtendDTO {

    @NotNull
    private Long assignmentId;
    @NotNull
    private int daysExtended;


}
