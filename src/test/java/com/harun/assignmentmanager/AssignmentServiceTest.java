package com.harun.assignmentmanager;



import com.harun.assignmentmanager.DTO.Assignment.AssignmentMapper;
import com.harun.assignmentmanager.DTO.Assignment.MoveAssignmentRequestDTO;
import com.harun.assignmentmanager.Entity.Assignment;
import com.harun.assignmentmanager.Entity.Course;
import com.harun.assignmentmanager.Exceptions.BadRequestException;
import com.harun.assignmentmanager.Exceptions.ResourceNotFoundException;
import com.harun.assignmentmanager.Repository.AssignmentRepository;
import com.harun.assignmentmanager.Repository.CourseRepository;
import com.harun.assignmentmanager.Service.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AssignmentServiceTest {

    private AssignmentRepository assignmentRepository;
    private CourseRepository courseRepository;
    private AssignmentMapper assignmentMapper;
    private AssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        assignmentRepository = Mockito.mock(AssignmentRepository.class);
        courseRepository = Mockito.mock(CourseRepository.class);
        assignmentMapper = new AssignmentMapper();

        assignmentService = new AssignmentService(assignmentRepository, courseRepository, assignmentMapper);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when assignment does not exist")
    void shouldThrowWhenAssignmentDoesNotExist() {

        when(assignmentRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> assignmentService.getAssignment(100L));

        verify(assignmentRepository).findById(100L);
    }

    @Test
    @DisplayName("Should throw BadRequestException when assignment does not belong to old course")
    void shouldThrowWhenAssignmentDoesNotBelongToOldCourse() {

        Course oldCourse = new Course(1L, "Old Course");
        Course newCourse = new Course(2L, "New Course");

        Assignment assignment = new Assignment();
        assignment.setId(10L);
        assignment.setTitle("Test Assignment");
        assignment.setDeadline(LocalDate.now().plusDays(1));
        assignment.setStatus(Assignment.Status.TODO);
        assignment.setCourse(newCourse); // intentionally wrong

        MoveAssignmentRequestDTO dto = new MoveAssignmentRequestDTO();
        dto.setAssignmentId(10L);
        dto.setOldCourseId(1L);
        dto.setNewCourseId(2L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(oldCourse));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(newCourse));
        when(assignmentRepository.findById(10L)).thenReturn(Optional.of(assignment));

        assertThrows(BadRequestException.class, () -> assignmentService.moveAssignmentBetweenCourses(dto));

        verify(courseRepository).findById(1L);
        verify(courseRepository).findById(2L);
        verify(assignmentRepository).findById(10L);
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BadRequestException when assignment is already in target course")
    void shouldThrowWhenAssignmentAlreadyInTargetCourse() {

        Course sameCourse = new Course(2L, "Same Course");

        Assignment assignment = new Assignment();
        assignment.setId(10L);
        assignment.setTitle("Assignment");
        assignment.setDeadline(LocalDate.now().plusDays(1));
        assignment.setStatus(Assignment.Status.TODO);
        assignment.setCourse(sameCourse);

        MoveAssignmentRequestDTO dto = new MoveAssignmentRequestDTO();
        dto.setAssignmentId(10L);
        dto.setOldCourseId(2L);
        dto.setNewCourseId(2L);

        when(courseRepository.findById(2L)).thenReturn(Optional.of(sameCourse));
        when(assignmentRepository.findById(10L)).thenReturn(Optional.of(assignment));

        assertThrows(BadRequestException.class, () -> assignmentService.moveAssignmentBetweenCourses(dto));

        verify(assignmentRepository, never()).save(any());
    }
}