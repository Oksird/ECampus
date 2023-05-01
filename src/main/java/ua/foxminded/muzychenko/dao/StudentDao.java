package ua.foxminded.muzychenko.dao;

import java.util.Optional;
import ua.foxminded.muzychenko.entity.StudentEntity;

public interface StudentDao extends CrudDao<StudentEntity, Long> {

    Optional<StudentEntity> findByCourse(String nameOfCourse);

    @Override
    void deleteById(Long id);

    void addToCourse(StudentEntity entity, String nameOfCourse);

    void removeFromCourse(StudentEntity entity, String nameOfCourse);
}
