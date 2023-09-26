package ua.foxminded.muzychenko.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.List;

@Repository
public interface TeacherRepository extends UserRepository<Teacher> {
    List<Teacher> findByCourses_CourseName(String courseName);

}
