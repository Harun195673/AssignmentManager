package com.harun.assignmentmanager.Service;

import com.harun.assignmentmanager.DTO.Assignment.AssignmentMapper;
import com.harun.assignmentmanager.DTO.Assignment.AssignmentResponseDTO;
import com.harun.assignmentmanager.DTO.Course.CourseMapper;
import com.harun.assignmentmanager.DTO.Course.CourseRequestDTO;
import com.harun.assignmentmanager.DTO.Course.CourseResponseDTO;
import com.harun.assignmentmanager.Entity.Assignment;
import com.harun.assignmentmanager.Entity.Course;
import com.harun.assignmentmanager.Exceptions.ConflictException;
import com.harun.assignmentmanager.Exceptions.ResourceNotFoundException;
import com.harun.assignmentmanager.Repository.AssignmentRepository;
import com.harun.assignmentmanager.Repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final AssignmentRepository assignmentRepository;
    private final CourseMapper courseMapper;
    private final AssignmentMapper assignmentMapper;



    public CourseService(CourseRepository courseRepository,
                         AssignmentRepository assignmentRepository,
                         CourseMapper courseMapper,
                         AssignmentMapper assignmentMapper) {
        this.courseRepository = courseRepository;
        this.assignmentRepository = assignmentRepository;
        this.courseMapper = courseMapper;
        this.assignmentMapper = assignmentMapper;
    }

    // CREATE
    public CourseResponseDTO createCourse(CourseRequestDTO courseDto) {
        Course course = courseMapper.toEntity(courseDto);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toDTO(savedCourse);
    }

    // GET ALL
    public List<CourseResponseDTO> getAllCourses() {

        List<Course> courses = courseRepository.findAll();
        List<CourseResponseDTO> result = new ArrayList<>();

        for (Course course : courses) {
            result.add(courseMapper.toDTO(course));
        }

        return result;
    }

    // GET BY ID
    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        return courseMapper.toDTO(course);
    }

    // DELETE
    public void deleteCourse(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        // ✅ CORRECT CHECK (no relation needed)
        boolean hasAssignments = assignmentRepository.existsByCourseId(id);

        if (hasAssignments) {
            throw new ConflictException("Cannot delete course with existing assignments");
        }

        courseRepository.delete(course);
    }





    public List<AssignmentResponseDTO> getAssignmentsByCourse(Long courseId) {
        // 1. check course exists
        // 2. fetch assignments by courseId
        // 3. map to DTO

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: "));

        List<Assignment> assignments = assignmentRepository.findByCourseId(courseId);
        return assignmentMapper.toDTOList(assignments);
    }













}