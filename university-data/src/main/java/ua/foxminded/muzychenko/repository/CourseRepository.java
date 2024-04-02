package ua.foxminded.muzychenko.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    Optional<Course> findByCourseName(String courseName);

    Page<Course> findByCourseNameContainingIgnoreCase(String courseNamePart, Pageable pageable);

    Optional<Course> findByTeacher(Teacher teacher);
}
