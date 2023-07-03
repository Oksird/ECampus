package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.StudentEntity;
import util.DataBaseSetUpper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class StudentDaoImplContainerTest {

    @Container
    private final static PostgreSQLContainer container = new PostgreSQLContainer<>("postgres:latest");

    private StudentDao studentDao;

    @BeforeEach
    void set_up() {
        DBConnector dbConnector = new DBConnector(
            container.getJdbcUrl(),
            container.getUsername(),
            container.getPassword()
        );
        studentDao = new StudentDaoImpl(dbConnector);
        DataBaseSetUpper.setUpDataBase(dbConnector);
    }

    @DisplayName("All students are found by course")
    @Test
    void findByCourseShouldReturnAllStudentOnSpecifiedCourse() {
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
    void findByCourseShouldReturnEmptyListIfCourseDoesntExist() {
        List<StudentEntity> students = studentDao.findByCourse("Computer Science");
        assertEquals(0, students.size());
    }

    @DisplayName("Student is added to course")
    @Test
    void addToCourseShouldAddSpecificStudentToSpecificCourse() {
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
    void removeFromCourseShouldRemoveSpecificStudentFromSpecificCourse() {
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
    void deleteByIdShouldDeleteSpecificStudent() {
        List<StudentEntity> expectedStudents = studentDao.findAll();
        expectedStudents.remove(new StudentEntity(1, 1, "John", "Brown"));

        studentDao.deleteById(1L);

        assertEquals(expectedStudents, studentDao.findAll());
    }

    @DisplayName("Student is updated correctly")
    @Test
    void updateShouldReplaceOldStudentEntityWithNewOne() {
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
    void findAllByPageShouldReturnAllStudentsOnTheCurrentPage() {
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
