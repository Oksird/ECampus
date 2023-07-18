package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.StudentEntity;

import java.util.List;

public interface StudentDao extends CrudDao<StudentEntity, Long> {

    List<StudentEntity> findByCourse(String nameOfCourse);

    void addToCourse(String nameOfCourse, Long id);

    void deleteFromCourse(Long id, String nameOfCourse);
}
