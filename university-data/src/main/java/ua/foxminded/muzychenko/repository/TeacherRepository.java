package ua.foxminded.muzychenko.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.Teacher;

@Repository
public interface TeacherRepository extends UserRepository<Teacher> {
}
