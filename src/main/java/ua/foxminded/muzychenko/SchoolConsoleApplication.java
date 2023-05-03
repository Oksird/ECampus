package ua.foxminded.muzychenko;

import java.sql.SQLException;
import java.sql.Statement;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.data.impl.CoursesGeneratorImpl;
import ua.foxminded.muzychenko.dao.data.impl.GroupsGeneratorImpl;
import ua.foxminded.muzychenko.dao.data.impl.StudentsGeneratorImpl;
import ua.foxminded.muzychenko.dao.impl.CourseDaoImpl;
import ua.foxminded.muzychenko.dao.impl.GroupDaoImpl;
import ua.foxminded.muzychenko.dao.impl.StudentDaoImpl;

public class SchoolConsoleApplication {

    public static void main(String[] args) throws SQLException {
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
            new GroupsGeneratorImpl(groupDao),
            new CoursesGeneratorImpl(courseDao),
            new StudentsGeneratorImpl(studentDao)
        );
        schoolApplicationFacade.runQueries();
        Statement statement = dbConnector.getConnection().createStatement();
    }
}
