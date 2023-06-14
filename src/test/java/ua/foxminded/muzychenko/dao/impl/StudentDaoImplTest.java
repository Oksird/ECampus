package ua.foxminded.muzychenko.dao.impl;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.StudentEntity;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;
import ua.foxminded.muzychenko.exception.WrongFilePathException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentDaoImplTest {

    private StudentDao studentDao;
    private static final String RESOURCES_PATH = "src/main/resources/";

    @BeforeEach
    void setUp() {

        DBConnector dbConnector = new DBConnector("/testDb.properties");
        ScriptRunner scriptRunner;

        studentDao = new StudentDaoImpl(dbConnector);

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

    @DisplayName("All students are found by course")
    @Test
    void findByCourse_shouldReturnAllStudentOnSpecifiedCourse() {
        List<StudentEntity> expectedStudents = new ArrayList<>(
            List.of(
                new StudentEntity(1, 1, "John", "Brown"),
                new StudentEntity(3,1,"Mija", "White"),
                new StudentEntity(4,1,"Lee", "Simpson"),
                new StudentEntity(6,2,"Jorge", "White"),
                new StudentEntity(9,3,"David", "Ivanko"))
        );
        List<StudentEntity> actualStudents = studentDao.findByCourse("Math");
        assertEquals(expectedStudents, actualStudents);
    }

    @DisplayName("Course doesn't exist")
    @Test
    void findByCourse_shouldReturnEmptyListIfCourseDoesntExist() {
        List<StudentEntity> students = studentDao.findByCourse("Computer Science");
        assertEquals(0, students.size());
    }

    @DisplayName("Student is added to course")
    @Test
    void addToCourse_shouldAddSpecificStudentToSpecificCourse() {
        StudentEntity student = new StudentEntity(
            13,
            1,
            "Maksym",
            "Muzychenko");
        studentDao.create(student);
        studentDao.addToCourse(student, "Math");

        List<StudentEntity> expectedStudents = new ArrayList<>(
            List.of(
                new StudentEntity(1, 1, "John", "Brown"),
                new StudentEntity(3,1,"Mija", "White"),
                new StudentEntity(4,1,"Lee", "Simpson"),
                new StudentEntity(6,2,"Jorge", "White"),
                new StudentEntity(9,3,"David", "Ivanko"),
                student)
        );

        assertEquals(expectedStudents, studentDao.findByCourse("Math"));
    }

    @DisplayName("Student is removed from course")
    @Test
    void removeFromCourse_shouldRemoveSpecificStudentFromSpecificCourse() {
        Optional<StudentEntity> student = studentDao.findById(1L);
        student.ifPresent(studentEntity -> studentDao.removeFromCourse(studentEntity, "Math"));
        List<StudentEntity> expectedStudents = new ArrayList<>(
            List.of(
                new StudentEntity(3,1,"Mija", "White"),
                new StudentEntity(4,1,"Lee", "Simpson"),
                new StudentEntity(6,2,"Jorge", "White"),
                new StudentEntity(9,3,"David", "Ivanko"))
        );

        assertEquals(expectedStudents, studentDao.findByCourse("Math"));
    }

    @DisplayName("Student is deleted by id")
    @Test
    void deleteById_shouldDeleteSpecificStudent() {
        List<StudentEntity> expectedStudents = studentDao.findAll();
        expectedStudents.remove(new StudentEntity(1, 1, "John", "Brown"));

        studentDao.deleteById(1L);

        assertEquals(expectedStudents, studentDao.findAll());
    }

    @DisplayName("Student is updated correctly")
    @Test
    void update_shouldReplaceOldStudentEntityWithNewOne() {
        List<StudentEntity> students = studentDao.findAll();
        StudentEntity firstStudent = students.get(0);
        StudentEntity expectedStudent = new StudentEntity(1, 1, "Sam", "Winchester");
        studentDao.update(firstStudent, expectedStudent);
        StudentEntity actualStudent = null;
        Optional<StudentEntity> optionalStudent = studentDao.findById(1L);
        if (optionalStudent.isPresent()){
            actualStudent = optionalStudent.get();
        }
        assertEquals(expectedStudent, actualStudent);
    }

    @DisplayName("Pagination")
    @Test
    void findAllByPage_shouldReturnAllStudentsOnTheCurrentPage() {
        long countOfPages = 4;
        long pageSize = 4;
        List<StudentEntity> studentsOnLastPage = studentDao.findAllByPage(countOfPages, pageSize);
        List<StudentEntity> actualStudents = new ArrayList<>();
        Optional<StudentEntity> student;
        for (int i = (int) (pageSize * 2); i >studentDao.findAll().size() ; i++) {
            student = studentDao.findById((long) i);
            student.ifPresent(actualStudents::add);
        }
        assertEquals(studentsOnLastPage, actualStudents);
    }
}
