package ua.foxminded.muzychenko;

import java.sql.SQLException;
import java.util.Random;

import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.impl.CoursesGeneratorImpl;
import ua.foxminded.muzychenko.dao.impl.GroupsGeneratorImpl;
import ua.foxminded.muzychenko.dao.impl.StudentsGeneratorImpl;
import ua.foxminded.muzychenko.dao.impl.CourseDaoImpl;
import ua.foxminded.muzychenko.dao.impl.GroupDaoImpl;
import ua.foxminded.muzychenko.dao.impl.StudentDaoImpl;

public class SchoolConsoleApplication {

    public static void main(String[] args) throws SQLException {
        Random random = new Random();
        DBCreator.createDB();
        DBConnector dbConnector = new DBConnector("/db.properties");
        StudentDao studentDao = new StudentDaoImpl(dbConnector);
        GroupDao groupDao = new GroupDaoImpl(dbConnector);
        CourseDao courseDao = new CourseDaoImpl(dbConnector);
        SchoolApplicationFacade schoolApplicationFacade = new SchoolApplicationFacade(
            groupDao,
            courseDao,
            studentDao,
            new DBCreator(dbConnector),
            new GroupsGeneratorImpl(groupDao, random),
            new CoursesGeneratorImpl(courseDao),
            new StudentsGeneratorImpl(studentDao, random),
            random);
        schoolApplicationFacade.runQueries();
    }
}
