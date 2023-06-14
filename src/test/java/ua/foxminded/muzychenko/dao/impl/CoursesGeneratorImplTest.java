package ua.foxminded.muzychenko.dao.impl;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.CoursesGenerator;
import ua.foxminded.muzychenko.entity.CourseEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;
import ua.foxminded.muzychenko.exception.WrongFilePathException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoursesGeneratorImplTest {

    private CourseDao courseDao;
    private CoursesGenerator coursesGenerator;
    private static final String RESOURCES_PATH = "src/main/resources/";

    @BeforeEach
    void setUp() {

        DBConnector dbConnector = new DBConnector("/testDb.properties");
        ScriptRunner scriptRunner;

        courseDao = new CourseDaoImpl(dbConnector);
        coursesGenerator = new CoursesGeneratorImpl(courseDao);

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

    @DisplayName("Courses were generated correctly")
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

    @DisplayName("Courses are inserted")
    @Test
    void insertCourses_shouldInsertMultipleCourses() {
        List<CourseEntity> insertedCourses = new ArrayList<>();
        insertedCourses.add(new CourseEntity(4L, "Physics", "Force"));
        insertedCourses.add(new CourseEntity(5L, "Chemistry", "Breaking Bad"));
        insertedCourses.add(new CourseEntity(6L, "Biology", "Flowers and animals"));

        coursesGenerator.insertCourses(insertedCourses);

        List<CourseEntity> actualCourses = courseDao.findAll();

        assertTrue(actualCourses.containsAll(insertedCourses));

    }
}
