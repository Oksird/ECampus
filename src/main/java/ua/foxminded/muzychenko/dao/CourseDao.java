package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseDao extends CrudDao<Course, UUID> {
    List<Course> findCoursesByUserIdAndUserType(UUID userId, UserType userType);

    Optional<Course> findByName(String courseName);
}
