package com.harun.assignmentmanager;


import com.harun.assignmentmanager.DTO.Assignment.AssignmentMapper;
import com.harun.assignmentmanager.DTO.Course.CourseMapper;
import com.harun.assignmentmanager.Entity.Course;

import com.harun.assignmentmanager.Exceptions.ConflictException;
import com.harun.assignmentmanager.Exceptions.ResourceNotFoundException;
import com.harun.assignmentmanager.Repository.AssignmentRepository;
import com.harun.assignmentmanager.Repository.CourseRepository;
import com.harun.assignmentmanager.Service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    private CourseRepository courseRepository;
    private AssignmentRepository assignmentRepository;
    private CourseMapper courseMapper;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseRepository = Mockito.mock(CourseRepository.class);
        assignmentRepository = Mockito.mock(AssignmentRepository.class);
        courseMapper = new CourseMapper();
        AssignmentMapper assignmentMapper = new AssignmentMapper();

        courseService = new CourseService(courseRepository, assignmentRepository, courseMapper, assignmentMapper);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when course does not exist")
    void shouldThrowWhenCourseDoesNotExist() {

        when(courseRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.deleteCourse(10L));

        verify(courseRepository).findById(10L);
        verify(assignmentRepository, never()).existsByCourseId(anyLong());
        verify(courseRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should throw ConflictException when course has assignments")
    void shouldThrowWhenCourseHasAssignments() {

        Course course = new Course();
        course.setId(1L);
        course.setName("Math");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(assignmentRepository.existsByCourseId(1L)).thenReturn(true);

        assertThrows(ConflictException.class, () -> courseService.deleteCourse(1L));

        verify(courseRepository).findById(1L);
        verify(assignmentRepository).existsByCourseId(1L);
        verify(courseRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should delete course when it has no assignments")
    void shouldDeleteWhenCourseHasNoAssignments() {

        Course course = new Course();
        course.setId(2L);
        course.setName("Physics");

        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(assignmentRepository.existsByCourseId(2L)).thenReturn(false);

        courseService.deleteCourse(2L);

        verify(courseRepository).findById(2L);
        verify(assignmentRepository).existsByCourseId(2L);
        verify(courseRepository).delete(course);
    }
}