package com.harun.assignmentmanager.Service;

import com.harun.assignmentmanager.DTO.Assignment.*;
import com.harun.assignmentmanager.Entity.Assignment;
import com.harun.assignmentmanager.Entity.Course;


import com.harun.assignmentmanager.Exceptions.BadRequestException;
import com.harun.assignmentmanager.Exceptions.ResourceNotFoundException;
import com.harun.assignmentmanager.Repository.AssignmentRepository;
import com.harun.assignmentmanager.Repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final AssignmentMapper assignmentMapper;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             CourseRepository courseRepository,
                             AssignmentMapper assignmentMapper) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.assignmentMapper = assignmentMapper;
    }

    public AssignmentResponseDTO createAssignment(AssignmentRequestDTO assignmentDTO) {

        Course course = courseRepository.findById(assignmentDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + assignmentDTO.getCourseId()));

        Assignment assignment = assignmentMapper.toEntity(assignmentDTO, course);
        Assignment saved = assignmentRepository.save(assignment);

        return assignmentMapper.toDTO(saved);
    }

    public Page<AssignmentResponseDTO> getAllAssignments(Pageable pageable) {

        Page<Assignment> assignments =
                assignmentRepository.findAll(pageable);

        return assignments.map(assignmentMapper::toDTO);
    }

    public AssignmentResponseDTO getAssignment(Long id) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));

        return assignmentMapper.toDTO(assignment);
    }

    public List<AssignmentResponseDTO> getOverdueAssignments() {

        List<Assignment> assignmentList =
                assignmentRepository.findByDeadlineBefore(LocalDate.now());

        List<AssignmentResponseDTO> DTOList = new ArrayList<>();

        for (Assignment assignment : assignmentList) {
            if (LocalDate.now().isAfter(assignment.getDeadline())) {
                DTOList.add(assignmentMapper.toDTO(assignment));
            }
        }
        return DTOList;
    }



    public Page<AssignmentResponseDTO> getOverdueAssignments(Pageable pageable) {

        Page<Assignment> assignments =
                assignmentRepository.findByDeadlineBefore(
                        LocalDate.now(),
                        pageable
                );

        return assignments.map(assignmentMapper::toDTO);
    }




    public Page<AssignmentResponseDTO> getAssignmentsByStatus(
            Assignment.Status status,
            Pageable pageable) {

        Page<Assignment> assignments =
                assignmentRepository.findAssignmentsByStatus(status, pageable);

        return assignments.map(assignmentMapper::toDTO);
    }

    public AssignmentResponseDTO updateAssignment(Long id, AssignmentRequestDTO dto) {

        Assignment currentAssignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + dto.getCourseId()));

        currentAssignment.setCourse(course);
        currentAssignment.setDeadline(dto.getDeadline());
        currentAssignment.setTitle(dto.getTitle());
        currentAssignment.setStatus(dto.getStatus());

        Assignment saved = assignmentRepository.save(currentAssignment);
        return assignmentMapper.toDTO(saved);
    }

    @Transactional
    public AssignmentResponseDTO moveAssignmentBetweenCourses(MoveAssignmentRequestDTO dto) {

        Course newCourse = courseRepository.findById(dto.getNewCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("New course not found with id: " + dto.getNewCourseId()));

        Course oldCourse = courseRepository.findById(dto.getOldCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Old course not found with id: " + dto.getOldCourseId()));

        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + dto.getAssignmentId()));

        if (!assignment.getCourse().getId().equals(oldCourse.getId())) {
            throw new BadRequestException("Assignment does not belong to the old course");
        }

        if (assignment.getCourse().getId().equals(newCourse.getId())) {
            throw new BadRequestException("Assignment is already in the target course");
        }

        assignment.setCourse(newCourse);
        Assignment savedAssignment = assignmentRepository.save(assignment);

        return assignmentMapper.toDTO(savedAssignment);
    }



    @Transactional
    public List<AssignmentResponseDTO> getCompletedAssignments() {

        List<Assignment> doneAssignments = assignmentRepository
                .findByStatus(Assignment.Status.DONE);

        return assignmentMapper.toDTOList(doneAssignments);
    }

    public void deleteAssignment(Long id) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));

        assignmentRepository.delete(assignment);
    }







    public AssignmentResponseDTO extendAssignment (AssignmentExtendDTO dto){

        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + dto.getAssignmentId()));

        LocalDate newDate = assignment.getDeadline().plusDays(dto.getDaysExtended());
        assignment.setDeadline(newDate);
        assignmentRepository.save(assignment);

        return assignmentMapper.toDTO(assignment);
    }


    @Transactional
    public List<AssignmentResponseDTO> extendAssignments(int daysExtended) {

        List<Assignment> assignmentList =
                assignmentRepository.findByStatus(Assignment.Status.OVERDUE);

        for (Assignment assignment : assignmentList) {
            assignment.setDeadline(
                    assignment.getDeadline().plusDays(daysExtended)
            );
        }

        return assignmentMapper.toDTOList(assignmentList);
    }



    @Transactional
    @Scheduled(fixedRate = 300000)
    public void markOverdueAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll();
        for (Assignment a : assignments) {
            if (a.getDeadline().isBefore(LocalDate.now()) && a.getStatus() != Assignment.Status.OVERDUE) {
                a.setStatus(Assignment.Status.OVERDUE);
                assignmentRepository.save(a); // explicitly save
            }
        }
    }



    public List<AssignmentResponseDTO> addAssignmentListToCourse (Long oldCourseId, Long newCourseId){

        Course oldCourse = courseRepository.findById(oldCourseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course was not found"));

        Course newCourse = courseRepository.findById(newCourseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course was not found"));

            return null;
    }


    public HashMap<Course, Integer> getCourseAssignmentCount() {
        List<Assignment> assignmentList = assignmentRepository.findAllAssignmentsWithCourse();
        HashMap<Course, Integer> map = new HashMap<>();

        for (Assignment assignment : assignmentList) {
            Course course = assignment.getCourse();
            map.put(course, map.getOrDefault(course, 0) + 1);
        }

        return map;
    }









}