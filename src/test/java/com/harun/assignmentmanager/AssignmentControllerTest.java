package com.harun.assignmentmanager;

import com.harun.assignmentmanager.Controller.AssignmentController;
import com.harun.assignmentmanager.DTO.Assignment.AssignmentRequestDTO;
import com.harun.assignmentmanager.Entity.Assignment;
import com.harun.assignmentmanager.Exceptions.GlobalExceptionHandler;
import com.harun.assignmentmanager.Exceptions.ResourceNotFoundException;
import com.harun.assignmentmanager.Service.AssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssignmentController.class)
@Import(GlobalExceptionHandler.class)
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.boot.test.mock.mockito.MockBean
    private AssignmentService assignmentService;

    @Test
    @DisplayName("Should return 404 when assignment is not found")
    void shouldReturn404WhenAssignmentNotFound() throws Exception {

        Mockito.when(assignmentService.getAssignment(99L))
                .thenThrow(new ResourceNotFoundException("Assignment not found with id: 99"));

        mockMvc.perform(get("/assignments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Assignment not found with id: 99"))
                .andExpect(jsonPath("$.path").value("/assignments/99"));
    }

    @Test
    @DisplayName("Should return 400 when validation fails on createAssignment")
    void shouldReturn400WhenValidationFails() throws Exception {

        AssignmentRequestDTO dto = new AssignmentRequestDTO();
        dto.setTitle(""); // invalid because @NotBlank
        dto.setDeadline(null); // invalid because @NotNull
        dto.setCourseId(null); // invalid because @NotNull
        dto.setStatus(Assignment.Status.TODO);

        mockMvc.perform(post("/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors.title").exists())
                .andExpect(jsonPath("$.validationErrors.deadline").exists())
                .andExpect(jsonPath("$.validationErrors.courseId").exists());
    }

    @Test
    @DisplayName("Should return 400 when enum status is invalid")
    void shouldReturn400WhenEnumStatusIsInvalid() throws Exception {

        mockMvc.perform(get("/assignments/status/INVALID_STATUS"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}