package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.entity.CourseEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class CourseDaoImplTest {

    private CourseDaoImpl courseDao;

    @BeforeEach
    void setUp() {
        DBConnector dbConnector = new DBConnector("/testdb.properties");
        courseDao = new CourseDaoImpl(dbConnector);

        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS courses (" +
                "course_id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "course_name VARCHAR(255)," +
                "course_description VARCHAR(255))");

            stmt.execute("DELETE FROM courses");

        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }

    }

    @Test
    void mapResultSetToEntity_shouldMapValidResultSetToEntity() throws SQLException {
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);
        Mockito.when(resultSetMock.getLong("course_id")).thenReturn(10L);
        Mockito.when(resultSetMock.getString("course_name")).thenReturn("Math");
        Mockito.when(resultSetMock.getString("course_description")).thenReturn("Numbers");

        CourseEntity course = courseDao.mapResultSetToEntity(resultSetMock);

        assertEquals(10, course.courseId());
        assertEquals("Math", course.courseName());
        assertEquals("Numbers", course.courseDescription());
    }

    @Test
    void mapResultSetToEntity_shouldMapNullValuesToEntity() throws SQLException {
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);
        Mockito.when(resultSetMock.getLong("course_id")).thenReturn(1L);
        Mockito.when(resultSetMock.getString("course_name")).thenReturn(null);
        Mockito.when(resultSetMock.getString("course_description")).thenReturn(null);

        CourseEntity course = courseDao.mapResultSetToEntity(resultSetMock);

        assertEquals(1L, course.courseId());
        assertNull(course.courseName());
        assertNull(course.courseDescription());
    }


    @Test
    void create_ShouldInsertCourseInCoursesTable() {
        CourseEntity course =
            new CourseEntity(1L, "Math", "Numbers and stuff");

        courseDao.create(course);

        List<CourseEntity> insertedCourses = courseDao.findAll();

        assertEquals(1, insertedCourses.size());
    }

    @Test
    void update_shouldUpdateCourseValuesInDatabase() {

        courseDao.create(new CourseEntity(0, "Math", "Numbers"));

        List<CourseEntity> courses = courseDao.findAll();

        CourseEntity mathCourse = courses.get(0);

        CourseEntity chemistryCourse = new CourseEntity(mathCourse.courseId(), "Chemistry", "Breaking bad");

        courseDao.update(mathCourse, chemistryCourse);

        assertEquals(courseDao.findAll().listIterator().next(),chemistryCourse);

    }


    @Test
    void deleteById_shouldDeleteOneCourseFromTable() {
        courseDao.create(new CourseEntity(1L, "Math", "Numbers"));
        assertEquals(1, courseDao.findAll().size());
        List<CourseEntity> courses = courseDao.findAll();
        if (courses.listIterator().hasNext()) {
            courseDao.deleteById(courses.listIterator().next().courseId());
        }
        assertEquals(0, courseDao.findAll().size());
    }

    @Test
    void deleteById_shouldNotDeleteAnythingWhenCourseIdDoesNotExist() {
        courseDao.create(new CourseEntity(1L, "Math", "Numbers"));
        assertEquals(1, courseDao.findAll().size());

        courseDao.deleteById(2L);

        assertEquals(1, courseDao.findAll().size());
    }

    @Test
    void deleteById_shouldDeleteCorrectCourseWhenMultipleCoursesExist() {
        courseDao.create(new CourseEntity(1L, "Math", "Numbers"));
        courseDao.create(new CourseEntity(2L, "Science", "Physics"));
        courseDao.create(new CourseEntity(3L, "History", "World"));

        assertEquals(3, courseDao.findAll().size());


        List<CourseEntity> courses = courseDao.findAll();
        long[] coursesIds = new long[courses.size()];
        for (int i = 0; i < courses.size(); i++) {
            coursesIds[i] = courses.get(i).courseId();
        }

        courseDao.deleteById(coursesIds[0]);

        assertEquals(2, courseDao.findAll().size());
        assertFalse(courses.stream().anyMatch(course -> course.courseId() == 2L));
    }
}
