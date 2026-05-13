package com.harun.assignmentmanager.Repository;

import com.harun.assignmentmanager.Entity.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;

public interface AssignmentRepository extends JpaRepository <Assignment, Long> {

    List<Assignment> findByDeadlineBefore(LocalDate date);

    List<Assignment> findByDeadlineAfterAndDeadlineBefore(LocalDate start, LocalDate end);

    List<Assignment> findByStatus(Assignment.Status status);

    List<Assignment> findByStatusInAndDeadlineBefore(List<Assignment.Status> statuses, LocalDate date);

    boolean existsByCourseId(Long courseId);

    List<Assignment> findByCourseId(Long courseId);

    @Query("SELECT a FROM Assignment a JOIN FETCH a.course")
    List<Assignment> findAllAssignmentsWithCourse();

    Page<Assignment> findByDeadlineBefore(
            LocalDate date,
            Pageable pageable
    );


    Page<Assignment> findAssignmentsByStatus(
            Assignment.Status status,
            Pageable pageable
    );




}
