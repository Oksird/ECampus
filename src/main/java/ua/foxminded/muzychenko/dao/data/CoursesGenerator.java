package ua.foxminded.muzychenko.dao.data;

import java.util.List;
import ua.foxminded.muzychenko.entity.CourseEntity;

public interface CoursesGenerator extends DataGenerator<CourseEntity> {

    void insertCourses(List<CourseEntity> courses);
}
