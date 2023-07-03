package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.entity.CourseEntity;
import util.DataBaseSetUpper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class CourseDaoImplContainerTest {

    @Container
    private final PostgreSQLContainer container = new PostgreSQLContainer("postgres:latest");

    private CourseDao courseDao;

    @BeforeEach
    void set_up() {
        DBConnector dbConnector = new DBConnector(
            container.getJdbcUrl(),
            container.getUsername(),
            container.getPassword()
        );
        courseDao = new CourseDaoImpl(dbConnector);
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
}
