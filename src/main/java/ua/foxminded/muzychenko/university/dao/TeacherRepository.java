package ua.foxminded.muzychenko.university.dao;

import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.university.entity.Teacher;

import java.util.List;

@Repository
public interface TeacherRepository extends UserRepository<Teacher> {
    List<Teacher> findByCourses_CourseName(String courseName);

}
