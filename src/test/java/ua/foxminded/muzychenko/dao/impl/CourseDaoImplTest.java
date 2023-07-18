package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.entity.CourseEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;
import util.DataBaseSetUpper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CourseDaoImplTest {

    private CourseDao courseDao;
    private CourseDao courseDaoException;

    @BeforeEach
    void setUp() {

        DBConnector dbConnector = new DBConnector("/testDb.properties");

        courseDao = new CourseDaoImpl1(dbConnector);
        DBConnector dbConnectorException = Mockito.mock(DBConnector.class);
        courseDaoException = new CourseDaoImpl1(dbConnectorException);
        try {
            when(dbConnectorException.getConnection()).thenThrow(new SQLException());
        } catch (SQLException sqlException) {
            throw new DataBaseRunTimeException(sqlException);
        }

        DataBaseSetUpper.setUpDataBase(dbConnector);
    }

    @DisplayName("Course was created")
    @Test
    void insertShouldCreateNewCourse() {
        CourseEntity testCourse = new CourseEntity(4, "Test", "It's just test");
        List<CourseEntity> expectedCourses = courseDao.findAll();
        expectedCourses.add(testCourse);
        courseDao.create(testCourse);
        assertEquals(expectedCourses, courseDao.findAll());
    }

    @DisplayName("Course was deleted")
    @Test
    void deleteByIdShouldDeleteSpecificCourse() {
        courseDao.create(new CourseEntity(4, "Test", "It's just test"));
        List<CourseEntity> expectedCourses = courseDao.findAll();
        expectedCourses.remove(expectedCourses.size() - 1);
        courseDao.deleteById(4L);
        assertEquals(expectedCourses, courseDao.findAll());
    }

    @DisplayName("Course was updated")
    @Test
    void updateShouldReplaceCourseWithProvidedOne() {
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

    @DisplayName("findById() throw sql exception")
    @Test
    void findByIdShouldThrowException() {
        assertThrows(DataBaseRunTimeException.class,
            () -> courseDaoException
                .findById(111111L)
        );
    }
}
