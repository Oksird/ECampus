package ua.foxminded.muzychenko.university.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.university.entity.Course;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    Optional<Course> findByCourseName(String courseName);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN c.students s LEFT JOIN c.teachers t " +
        "WHERE s.userId = :userId OR t.userId = :userId")
    Set<Course> findUsersCourses(UUID userId);

    Page<Course> findByCourseNameContainingIgnoreCase(String courseNamePart, Pageable pageable);
}
