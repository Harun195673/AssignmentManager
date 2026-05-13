package com.harun.assignmentmanager.Controller;

import com.harun.assignmentmanager.DTO.Assignment.AssignmentResponseDTO;
import com.harun.assignmentmanager.DTO.Course.CourseRequestDTO;
import com.harun.assignmentmanager.DTO.Course.CourseResponseDTO;
import com.harun.assignmentmanager.Service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @PostMapping
    public CourseResponseDTO createCourse(@RequestBody CourseRequestDTO dto) {
        return courseService.createCourse(dto);
    }


    @GetMapping
    public List<CourseResponseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }


    @GetMapping("/{id}")
    public CourseResponseDTO getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }


    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }


    @GetMapping("/{id}/assignments")
    public List<AssignmentResponseDTO> getAssignmentsByCourse(@PathVariable Long id) {
        return courseService.getAssignmentsByCourse(id);
    }






}