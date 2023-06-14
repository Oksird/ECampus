package ua.foxminded.muzychenko.dao.impl;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.entity.CourseEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;
import ua.foxminded.muzychenko.exception.WrongFilePathException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseDaoImplTest {

    private CourseDao courseDao;
    private static final String RESOURCES_PATH = "src/main/resources/";

    @BeforeEach
    void setUp() {

        DBConnector dbConnector = new DBConnector("/testDb.properties");
        ScriptRunner scriptRunner;

        courseDao = new CourseDaoImpl(dbConnector);

        try {
            scriptRunner = new ScriptRunner(dbConnector.getConnection());
        } catch (SQLException sqlException) {
            throw new DataBaseRunTimeException(sqlException);
        }

        try {
            FileReader createTablesSQLScriptFile = new FileReader(RESOURCES_PATH + "createTables.sql");
            FileReader generateDataSQLScriptFile = new FileReader(RESOURCES_PATH + "generateTestData.sql");
            FileReader deleteAllDataFromDataBaseSQLFile
                = new FileReader(RESOURCES_PATH + "deleteAllDataFromDataBases.sql");
            scriptRunner.runScript(createTablesSQLScriptFile);
            scriptRunner.runScript(deleteAllDataFromDataBaseSQLFile);
            scriptRunner.runScript(generateDataSQLScriptFile);
        } catch (FileNotFoundException fileNotFoundException) {
            throw new WrongFilePathException(fileNotFoundException.getMessage());
        }
    }

    @DisplayName("Course was created")
    @Test
    void insert_shouldCreateNewCourse() {
        CourseEntity testCourse = new CourseEntity(4, "Test", "It's just test");
        List<CourseEntity> expectedCourses = courseDao.findAll();
        expectedCourses.add(testCourse);
        courseDao.create(testCourse);
        assertEquals(expectedCourses, courseDao.findAll());
    }

    @DisplayName("Course was deleted")
    @Test
    void deleteById_shouldDeleteSpecificCourse() {
        courseDao.create(new CourseEntity(4, "Test", "It's just test"));
        List<CourseEntity> expectedCourses = courseDao.findAll();
        expectedCourses.remove(expectedCourses.size() - 1);
        courseDao.deleteById(4L);
        assertEquals(expectedCourses, courseDao.findAll());
    }

    @DisplayName("Course was updated")
    @Test
    void update_shouldReplaceCourseWithProvidedOne() {
        List<CourseEntity> expectedCourses = courseDao.findAll();
        CourseEntity testCourse = new CourseEntity(3, "Replaced course", "TEST");
        Optional<CourseEntity> optionalCourse = courseDao.findById(3L);
        CourseEntity actualCourse = null;
        if (optionalCourse.isPresent()) {
            actualCourse = optionalCourse.get();
        }
        expectedCourses.remove(actualCourse);
        expectedCourses.add(testCourse);
        courseDao.update(actualCourse, testCourse);
        assertEquals(expectedCourses, courseDao.findAll());
    }
}
