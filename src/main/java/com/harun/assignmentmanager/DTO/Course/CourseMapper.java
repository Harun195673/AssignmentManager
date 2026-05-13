package com.harun.assignmentmanager.DTO.Course;

import com.harun.assignmentmanager.Entity.Course;
import org.springframework.stereotype.Component;


@Component
public class CourseMapper {

    // RequestDTO → Entity
    public  Course toEntity(CourseRequestDTO dto) {
        Course course = new Course();

        course.setName(dto.getName());

        return course;
    }

    // Entity → ResponseDTO
    public  CourseResponseDTO toDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();

        dto.setId(course.getId());
        dto.setName(course.getName());

        return dto;
    }
}