package com.harun.assignmentmanager.DTO.Assignment;

import com.harun.assignmentmanager.Entity.Assignment;
import com.harun.assignmentmanager.Entity.Course;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssignmentMapper {

    // RequestDTO → Entity
    public Assignment toEntity(AssignmentRequestDTO dto, Course course) {
        Assignment assignment = new Assignment();

        assignment.setTitle(dto.getTitle());
        assignment.setDeadline(dto.getDeadline());
        assignment.setStatus(dto.getStatus());
        assignment.setCourse(course);


        return assignment;
    }

    // Entity → ResponseDTO
    public  AssignmentResponseDTO toDTO(Assignment assignment) {
        AssignmentResponseDTO dto = new AssignmentResponseDTO();

        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setDeadline(assignment.getDeadline());
        dto.setStatus(assignment.getStatus().name());


        if (assignment.getCourse() != null) {
            dto.setCourseId(assignment.getCourse().getId());
            dto.setCourseName(assignment.getCourse().getName());
        }

        return dto;
    }


    // EntityList → ResponseDTOList
    public  List<AssignmentResponseDTO> toDTOList(List<Assignment> assignmentList) {

        List<AssignmentResponseDTO> assignmentResponseDTOList = new ArrayList<>();

        for (Assignment assignment: assignmentList){
               assignmentResponseDTOList.add(this.toDTO(assignment));
        }

        return assignmentResponseDTOList;
    }















}