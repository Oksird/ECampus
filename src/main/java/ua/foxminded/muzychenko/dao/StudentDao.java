package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.StudentEntity;

import java.util.List;

public interface StudentDao extends CrudDao<StudentEntity, Long> {

    List<StudentEntity> findByCourse(String nameOfCourse);

    @Override
    void deleteById(Long id);

    void addToCourse(StudentEntity entity, String nameOfCourse);

    void removeFromCourse(StudentEntity entity, String nameOfCourse);
}
