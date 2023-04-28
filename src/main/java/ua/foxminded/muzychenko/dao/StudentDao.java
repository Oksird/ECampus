package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.StudentEntity;

import java.util.Optional;

public interface StudentDao extends CrudDao<StudentEntity, Long> {
    Optional<StudentEntity> findByCourse(String nameOfCourse);
}
