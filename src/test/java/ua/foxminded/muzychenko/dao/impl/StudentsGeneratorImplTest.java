package ua.foxminded.muzychenko.dao.impl;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.StudentsGenerator;
import ua.foxminded.muzychenko.entity.StudentEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;
import ua.foxminded.muzychenko.exception.WrongFilePathException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentsGeneratorImplTest {

    private  final Random random = new Random();
    private StudentDao studentDao;
    private StudentsGenerator studentsGenerator;
    private static final String RESOURCES_PATH = "src/main/resources/";

    @BeforeEach
    void setUp() {

        DBConnector dbConnector = new DBConnector("/testDb.properties");
        ScriptRunner scriptRunner;

        studentDao = new StudentDaoImpl(dbConnector);
        studentsGenerator = new StudentsGeneratorImpl(studentDao, new Random());

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

    @DisplayName("Students were generated correctly")
    @Test
    void generateData_shouldReturnNotNullListOfStudentsWithRandomNames() {
        List<String> firstNames = new ArrayList<>(
            List.of("Brittany", "Ivan", "Shannon", "Valerie", "Juan", "Cleo", "Landry", "Melani",
                "Kelly", "Luella", "Lyric", "Jon", "Rex", "Eddie", "Nixon", "Kendall", "Arthur",
                "Harvey", "Vincent", "Le"));
        List<String> lastNames = new ArrayList<>(
            List.of("Porter", "Hendrix", "Hogan", "Vance", "Page", "House", "Miles", "Cunningham",
                "Booker", "Barrett", "Larsen", "Reyes", "Brown", "Stewart", "Archer", "Santos", "Wade",
                "Turing", "Smith", "Walker"));
        List<StudentEntity> students = studentsGenerator.generateData();
        StudentEntity student = students.get(random.nextInt(students.size()));
        assertInstanceOf(
            student.getClass(),
            new StudentEntity(
                random.nextInt(students.size()),
                random.nextInt(students.size()),
                firstNames.get(random.nextInt(firstNames.size())),
                lastNames.get(random.nextInt(lastNames.size())))
        );
        assertNotNull(student);
        assertNotNull(students);
        assertTrue(firstNames.contains(students.get(1).firstName()));
        assertTrue(lastNames.contains(students.get(1).lastName()));
    }

    @DisplayName("Students were inserted correctly")
    @Test
    void insertStudents_shouldInsertStudentsCorrectly() {

        StudentEntity student1 = new StudentEntity(13, 1, "John", "Doe");
        StudentEntity student2 = new StudentEntity(14, 1, "Jane", "Doe");

        List<StudentEntity> students = List.of(student1, student2);

        studentsGenerator.insertStudents(students);

        Optional<StudentEntity> testStudent1 = studentDao.findById(13L);
        Optional<StudentEntity> testStudent2 = studentDao.findById(14L);
        StudentEntity expectedStudent1 = null;
        StudentEntity expectedStudent2 = null;
        if (testStudent1.isPresent() && testStudent2.isPresent()) {
            expectedStudent1 = testStudent1.get();
            expectedStudent2 = testStudent2.get();
        }
        assertEquals(expectedStudent1, student1);
        assertEquals(expectedStudent2, student2);
    }
}
