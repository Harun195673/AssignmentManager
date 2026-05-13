package com.harun.assignmentmanager.Controller;

import com.harun.assignmentmanager.DTO.Assignment.AssignmentExtendDTO;
import com.harun.assignmentmanager.DTO.Assignment.AssignmentRequestDTO;
import com.harun.assignmentmanager.DTO.Assignment.AssignmentResponseDTO;
import com.harun.assignmentmanager.DTO.Assignment.MoveAssignmentRequestDTO;
import com.harun.assignmentmanager.Entity.Assignment;
import com.harun.assignmentmanager.Entity.Course;
import com.harun.assignmentmanager.Service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Validated
@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }


    /// CREATE
    @PostMapping
    public AssignmentResponseDTO createAssignment(@RequestBody @Valid AssignmentRequestDTO assignmentDTO) {
        return assignmentService.createAssignment(assignmentDTO);
    }


    /// READ
    @GetMapping
    public Page<AssignmentResponseDTO> getAllAssignments(Pageable pageable) {
        return assignmentService.getAllAssignments(pageable);
    }

    @GetMapping("/overdue")
    public List<AssignmentResponseDTO> getOverdueAssignments() {
        return assignmentService.getOverdueAssignments();
    }

    @GetMapping("/overduePageable")
    public Page<AssignmentResponseDTO> getOverdueAssignments(Pageable pageable) {
        return assignmentService.getOverdueAssignments(pageable);
    }


    @GetMapping("/status/{status}")
    public Page<AssignmentResponseDTO> getAssignmentsByStatus(
            @PathVariable Assignment.Status status,
            Pageable pageable) {

        return assignmentService.getAssignmentsByStatus(status, pageable);
    }

    @GetMapping("/{assignmentId}")
    public AssignmentResponseDTO getAssignmentById(@PathVariable Long assignmentId) {
        return assignmentService.getAssignment(assignmentId);
    }

    @GetMapping("/completed")
    public List<AssignmentResponseDTO> getCompletedAssignments() {
        return assignmentService.getCompletedAssignments();
    }


    /// UPDATE
    @PutMapping("/{assignmentId}")
    public AssignmentResponseDTO updateAssignment(@PathVariable Long assignmentId,
                                                  @RequestBody AssignmentRequestDTO dto) {
        return assignmentService.updateAssignment(assignmentId, dto);
    }

    /**
     * Move an assignment from one course to another.
     * Validates old/new course and assignment existence.
     */
    @PutMapping("/move")
    public AssignmentResponseDTO moveAssignmentBetweenCourses(@RequestBody MoveAssignmentRequestDTO dto) {
        return assignmentService.moveAssignmentBetweenCourses(dto);
    }

    /**
     * Extend a single assignment's deadline.
     */
    @PutMapping("/extend")
    public AssignmentResponseDTO extendAssignment(@RequestBody AssignmentExtendDTO dto) {
        return assignmentService.extendAssignment(dto);
    }

    /**
     * Extend all overdue assignments by a given number of days.
     */
    @PutMapping("/extend-overdue/{daysExtended}")
    public List<AssignmentResponseDTO> extendOverdueAssignments(@PathVariable int daysExtended) {
        return assignmentService.extendAssignments(daysExtended);
    }


    /// STATS
    @GetMapping("/course-assignment-count")
    public HashMap<Course, Integer> getCourseAssignmentCount() {
        // Ideally should return a DTO, not Course entity directly
        return assignmentService.getCourseAssignmentCount();
    }


    @DeleteMapping("/{assignmentId}")
    public void deleteAssignment(@PathVariable Long assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
    }
}