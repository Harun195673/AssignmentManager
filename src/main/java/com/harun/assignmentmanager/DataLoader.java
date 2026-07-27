package com.harun.assignmentmanager;

import com.harun.assignmentmanager.Entity.Assignment;
import com.harun.assignmentmanager.Entity.Course;
import com.harun.assignmentmanager.Repository.AssignmentRepository;
import com.harun.assignmentmanager.Repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(
            CourseRepository courseRepository,
            AssignmentRepository assignmentRepository
    ) {

        return args -> {

            // =========================
            // COURSES
            // =========================

            Course javaCourse = courseRepository.save(
                    new Course(null, "Java Backend")
            );

            Course springCourse = courseRepository.save(
                    new Course(null, "Spring Boot")
            );

            Course databaseCourse = courseRepository.save(
                    new Course(null, "Databases")
            );



            // =========================
            // ASSIGNMENTS
            // =========================

            assignmentRepository.save(
                    new Assignment(
                            null,
                            "Build REST API",
                            LocalDate.now().plusDays(5),
                            Assignment.Status.IN_PROGRESS,
                            springCourse
                    )
            );

            assignmentRepository.save(
                    new Assignment(
                            null,
                            "Learn JPA Relationships",
                            LocalDate.now().plusDays(10),
                            Assignment.Status.TODO,
                            javaCourse
                    )
            );

            assignmentRepository.save(
                    new Assignment(
                            null,
                            "Database Normalization",
                            LocalDate.now().minusDays(2),
                            Assignment.Status.OVERDUE,
                            databaseCourse
                    )
            );

            assignmentRepository.save(
                    new Assignment(
                            null,
                            "Pagination Practice",
                            LocalDate.now().plusDays(3),
                            Assignment.Status.DONE,
                            springCourse
                    )
            );

            assignmentRepository.save(
                    new Assignment(
                            null,
                            "Debug LazyInitializationException",
                            LocalDate.now().plusDays(1),
                            Assignment.Status.IN_PROGRESS,
                            javaCourse
                    )
            );

            System.out.println("Sample demo data loaded.");
        };
    }
}