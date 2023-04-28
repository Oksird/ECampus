package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.CourseEntity;
import ua.foxminded.muzychenko.entity.StudentEntity;

public interface CourseDao extends CrudDao<CourseEntity, Long> {
    void addStudent(CourseEntity courseEntity, StudentEntity studentEntity);
}
