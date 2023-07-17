package ua.foxminded.muzychenko;

import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.StudentEntity;

@Component
public class SchoolApplicationFacade {

    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;

    public SchoolApplicationFacade(GroupDao groupDao, CourseDao courseDao,
                                   StudentDao studentDao) {
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.studentDao = studentDao;

    }

    public void runQueries() {
        StudentEntity student = new StudentEntity(
            1L,
            2L,
            "Max",
            "Muzychenko");
        groupDao.findGroupWithLessOrEqualStudents(10);
        studentDao.findByCourse("Math");
        studentDao.create(student);
        studentDao.deleteById(3L);
        studentDao.addToCourse(student, "Math");
        studentDao.findByCourse("Math");
    }
}
