package ua.foxminded.muzychenko.dao;

import java.util.List;
import ua.foxminded.muzychenko.entity.StudentEntity;

public interface StudentsGenerator extends DataGenerator<StudentEntity> {

    void insertStudents(List<StudentEntity> students);
}
