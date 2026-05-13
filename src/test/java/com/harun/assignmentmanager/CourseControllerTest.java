package com.harun.assignmentmanager;


import com.harun.assignmentmanager.Controller.CourseController;
import com.harun.assignmentmanager.Exceptions.ConflictException;
import com.harun.assignmentmanager.Exceptions.GlobalExceptionHandler;
import com.harun.assignmentmanager.Exceptions.ResourceNotFoundException;
import com.harun.assignmentmanager.Service.CourseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Import(GlobalExceptionHandler.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @org.springframework.boot.test.mock.mockito.MockBean
    private CourseService courseService;

    @Test
    @DisplayName("Should return 404 when course is not found")
    void shouldReturn404WhenCourseNotFound() throws Exception {

        Mockito.doThrow(new ResourceNotFoundException("Course not found with id: 5"))
                .when(courseService).deleteCourse(5L);

        mockMvc.perform(delete("/courses/5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Course not found with id: 5"))
                .andExpect(jsonPath("$.path").value("/courses/5"));
    }

    @Test
    @DisplayName("Should return 409 when deleting course with assignments")
    void shouldReturn409WhenCourseHasAssignments() throws Exception {

        Mockito.doThrow(new ConflictException("Cannot delete course with existing assignments"))
                .when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Cannot delete course with existing assignments"))
                .andExpect(jsonPath("$.path").value("/courses/1"));
    }
}