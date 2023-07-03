package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.StudentsGenerator;
import ua.foxminded.muzychenko.entity.StudentEntity;
import util.DataBaseSetUpper;

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

    @BeforeEach
    void setUp() {

        DBConnector dbConnector = new DBConnector("/testDb.properties");

        studentDao = new StudentDaoImpl(dbConnector);
        studentsGenerator = new StudentsGeneratorImpl(studentDao, new Random());

        DataBaseSetUpper.setUpDataBase(dbConnector);
    }

    @DisplayName("Students were generated correctly")
    @Test
    void generateDataShouldReturnNotNullListOfStudentsWithRandomNames() {
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
    void insertStudentsShouldInsertStudentsCorrectly() {

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
