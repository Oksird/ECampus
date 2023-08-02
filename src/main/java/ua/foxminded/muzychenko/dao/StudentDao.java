package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.Student;

import java.util.List;
import java.util.UUID;

public interface StudentDao extends UserDao<Student, UUID> {

    List<Student> findByCourse(String nameOfCourse);

    List<Student> findByGroup(String nameOfGroup);

    void addToCourse(UUID id, String courseName);
    void addToGroup(UUID id, String groupName);

    void deleteFromCourse(UUID id, String nameOfCourse);

    void deleteFromGroup(UUID id, String nameOfGroup);
}
