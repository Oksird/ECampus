package ua.foxminded.muzychenko;

import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.CoursesGenerator;
import ua.foxminded.muzychenko.dao.GroupsGenerator;
import ua.foxminded.muzychenko.dao.StudentsGenerator;
import ua.foxminded.muzychenko.entity.StudentEntity;

public class SchoolApplicationFacade {

    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final DBCreator dbCreator;
    private final GroupsGenerator groupsGenerator;
    private final CoursesGenerator coursesGenerator;
    private final StudentsGenerator studentsGenerator;

    public SchoolApplicationFacade(GroupDao groupDao, CourseDao courseDao,
                                   StudentDao studentDao, DBCreator dbCreator, GroupsGenerator groupsGenerator,
                                   CoursesGenerator coursesGenerator, StudentsGenerator studentsGenerator) {
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.studentDao = studentDao;
        this.dbCreator = dbCreator;
        this.groupsGenerator = groupsGenerator;
        this.coursesGenerator = coursesGenerator;
        this.studentsGenerator = studentsGenerator;
    }

    public void runQueries() {
        StudentEntity student = new StudentEntity(
            1L,
            2L,
            "Max",
            "Muzychenko");
        dbCreator.createTables();
        groupsGenerator.insertGroups(groupsGenerator.generateData());
        coursesGenerator.insertCourses(coursesGenerator.generateData());
        studentsGenerator.insertStudents(studentsGenerator.generateData());
        groupDao.findGroupWithLessOrEqualStudents(10);
        studentDao.findByCourse("Math");
        studentDao.create(student);
        studentDao.deleteById(2L);
        studentDao.addToCourse(student, "Math");
        studentDao.findByCourse("Math");

    }
}
