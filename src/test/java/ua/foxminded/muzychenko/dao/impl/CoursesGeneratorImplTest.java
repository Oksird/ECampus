package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.CoursesGenerator;
import ua.foxminded.muzychenko.entity.CourseEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoursesGeneratorImplTest {

    private CourseDao courseDao;
    private CoursesGenerator coursesGenerator;

    @BeforeEach
    void setUp() {
        DBConnector dbConnector = new DBConnector("/testdb.properties");
        courseDao = new CourseDaoImpl(dbConnector);
        coursesGenerator = new CoursesGeneratorImpl(courseDao);

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
    void generateData_shouldReturnListOfCoursesWithCorrectNamesAndDescriptions() {

        int countOfCourses = 10;

        List<String> courseNames = new ArrayList<>(
            List.of("Science", "Math", "English", "History", "Art", "Music", "Astronomy",
                "Physical Education", "Reading", "Geography"));
        List<String> courseDescriptions = new ArrayList<>(List.of(
            "Systematic endeavor that builds and organizes knowledge in the form of testable"
                + " explanations and predictions about the universe",
            "Area of knowledge that includes the topics of numbers, formulas and related structures,"
                + " shapes and the spaces in which they are contained,"
                + " and quantities and their changes",
            "Study of literature, media and language in which students critically and creatively engage"
                + " with a variety of texts in all language modes",
            "Academic discipline which uses narrative to describe, examine, question, and analyze"
                + " past events, and investigate their patterns of cause and effect",
            "Collection of disciplines which produce artworks (art as objects) that are compelled"
                + " by a personal drive (art as activity) and convey a message, mood, or symbolism"
                + " for the perceive to interpret (art as experience)",
            "Music as a school subject typically involves the study of various aspects of music,"
                + " including music theory, history, composition, and performance",
            "Scientific study of celestial objects such as stars, planets, galaxies, and other"
                + " phenomena that exist outside of the Earth's atmosphere",
            "Provides cognitive content and instruction designed to develop motor skills, knowledge,"
                + " and behaviors for physical activity and physical fitness",
            "Involves the development of critical thinking skills, such as analyzing, synthesizing,"
                + " and evaluating information. Students learn to distinguish between fact and opinion,"
                + " draw inferences and conclusions, and evaluate the credibility of sources.",
            "Study of the Earth's physical features, natural environment, and human societies and their"
                + " relationships with the physical world"));

        List<CourseEntity> expectedCourses = new ArrayList<>();

        for (int i = 0; i < countOfCourses; i++) {
            expectedCourses.add(new CourseEntity(i+1, courseNames.get(i), courseDescriptions.get(i)));
        }

        assertEquals(coursesGenerator.generateData(), expectedCourses);

    }

    @Test
    void insertCourses_shouldInsertsCoursesCorrectly() {
        CourseEntity course1 =
            new CourseEntity(1L, "Math", "Numbers and stuff");
        CourseEntity course2 =
            new CourseEntity(2L, "English", "Language and literature");

        List<CourseEntity> courses = List.of(course1, course2);

        coursesGenerator.insertCourses(courses);

        List<CourseEntity> insertedCourses = courseDao.findAll();

        assertEquals(2, insertedCourses.size());
    }

    @Test
    void insertCourses_shouldInsertSingleCourse() {
        CourseEntity course = new CourseEntity(0L, "Mathematics", "Numbers and stuff");

        coursesGenerator.insertCourses(List.of(course));

        List<CourseEntity> insertedCourses = courseDao.findAll();

        assertEquals(1, insertedCourses.size());
        assertEquals(course.courseName(), insertedCourses.get(0).courseName());
    }

    @Test
    void insertCourses_shouldInsertMultipleCourses() {
        List<CourseEntity> courses = new ArrayList<>();
        courses.add(new CourseEntity(0L, "Physics", "Force"));
        courses.add(new CourseEntity(0L, "Chemistry", "Breaking Bad"));
        courses.add(new CourseEntity(0L, "Biology", "Flowers and animals"));

        coursesGenerator.insertCourses(courses);

        List<CourseEntity> insertedCourses = courseDao.findAll();

        assertEquals(courses.size(), insertedCourses.size());

        for (CourseEntity expectedCourse : courses) {
            assertTrue(insertedCourses.stream()
                .anyMatch(actualCourse -> actualCourse.courseName().equals(expectedCourse.courseName())));
        }
    }

}
