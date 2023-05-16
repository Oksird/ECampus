package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.StudentsGenerator;
import ua.foxminded.muzychenko.entity.StudentEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StudentsGeneratorImplTest {

    private final StudentsGenerator studentsGenerator = new StudentsGeneratorImpl(
        new StudentDaoImpl(new DBConnector()), new Random()
    );

    @Test
    void generateData_shouldReturnNotNullListOfStudentsWithRandomNames() {
        Random random = new Random();
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

    @Test
    void insertStudents() {
    }
}