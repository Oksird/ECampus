package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.Teacher;

import java.util.List;
import java.util.UUID;

public interface TeacherDao extends UserDao<Teacher, UUID> {
    List<Teacher> findByCourse(String courseName);

    void addToCourse(UUID id, String courseName);

    void excludeFromCourse(UUID id, String courseName);
}
