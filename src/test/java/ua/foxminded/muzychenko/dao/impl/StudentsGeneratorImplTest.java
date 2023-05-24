package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.GroupsGenerator;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.dao.StudentsGenerator;
import ua.foxminded.muzychenko.entity.GroupEntity;
import ua.foxminded.muzychenko.entity.StudentEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class StudentsGeneratorImplTest {

    private StudentDao studentDao;
    private StudentsGenerator studentsGenerator;
    private GroupsGenerator groupsGenerator;
    private GroupDao groupDao;

    @BeforeEach
    void setUp() {
        DBConnector dbConnector = new DBConnector("/testdb.properties");
        studentDao = new StudentDaoImpl(dbConnector);
        groupDao = new GroupDaoImpl(dbConnector);
        groupsGenerator = new GroupsGeneratorImpl(groupDao, new Random());
        studentsGenerator = new StudentsGeneratorImpl(studentDao, new Random());

        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS groups (" +
                    "group_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "group_name VARCHAR(50) NOT NULL UNIQUE)"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS students (" +
                    "student_id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "group_id INTEGER, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "FOREIGN KEY (group_id) REFERENCES groups(group_id))"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS student_courses (" +
                    "student_id BIGINT, " +
                    "course_id BIGINT, " +
                    "PRIMARY KEY (student_id, course_id), " +
                    "FOREIGN KEY (student_id) REFERENCES students(student_id), " +
                    "FOREIGN KEY (course_id) REFERENCES courses(course_id))"
            );

            stmt.execute("DELETE FROM student_courses");
            stmt.execute("DELETE FROM students");
            stmt.execute("DELETE FROM groups");

        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    }

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
    void insertStudents_shouldInsertStudentsCorrectly() {
        GroupEntity group1 = new GroupEntity(0L, "CS-01");
        GroupEntity group2 = new GroupEntity(0L, "CS-02");

        List<GroupEntity> groups = List.of(group1, group2);

        groupsGenerator.insertGroups(groups);

        List<GroupEntity> insertedGroups = groupDao.findAll();
        assertEquals(groups.size(), insertedGroups.size());

        StudentEntity student1 = new StudentEntity(0L, insertedGroups.get(0).groupId(), "John", "Doe");
        StudentEntity student2 = new StudentEntity(0L, insertedGroups.get(1).groupId(), "Jane", "Doe");

        List<StudentEntity> students = List.of(student1, student2);

        studentsGenerator.insertStudents(students);
        List<StudentEntity> insertedStudents = studentDao.findAll();
        assertEquals(students.size(), insertedStudents.size());
    }

    @Test
    void insertStudents_shouldInsertEmptyListOfStudents() {
        List<StudentEntity> students = new ArrayList<>();

        studentsGenerator.insertStudents(students);

        List<StudentEntity> insertedStudents = studentDao.findAll();
        assertEquals(0, insertedStudents.size());
    }

    @Test
    void insertStudents_shouldInsertMultipleStudents() {
        GroupEntity group1 = new GroupEntity(0L, "CS-01");
        GroupEntity group2 = new GroupEntity(0L, "CS-02");

        List<GroupEntity> groups = List.of(group1, group2);
        groupsGenerator.insertGroups(groups);

        List<GroupEntity> insertedGroups = groupDao.findAll();
        assertEquals(groups.size(), insertedGroups.size());

        List<StudentEntity> students = new ArrayList<>();
        students.add(new StudentEntity(0L, insertedGroups.get(0).groupId(), "John", "Doe"));
        students.add(new StudentEntity(0L, insertedGroups.get(0).groupId(), "Jane", "Doe"));
        students.add(new StudentEntity(0L, insertedGroups.get(1).groupId(), "Alex", "Smith"));

        studentsGenerator.insertStudents(students);

        List<StudentEntity> insertedStudents = studentDao.findAll();
        assertEquals(students.size(), insertedStudents.size());

        for (int i = 0; i < students.size(); i++) {
            StudentEntity expectedStudent = students.get(i);
            StudentEntity actualStudent = insertedStudents.get(i);

            assertEquals(expectedStudent.groupId(), actualStudent.groupId());
            assertEquals(expectedStudent.firstName(), actualStudent.firstName());
            assertEquals(expectedStudent.lastName(), actualStudent.lastName());
        }
    }
}
