package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.Course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseDao extends CrudDao<Course, UUID> {
    List<Course> findCoursesByUserIdAndUserType(UUID userId);

    Optional<Course> findByName(String courseName);
}
