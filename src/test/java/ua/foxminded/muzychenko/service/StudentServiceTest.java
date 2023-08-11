package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(TestConfig.class)
class StudentServiceTest {
    @MockBean
    private StudentDao studentDao;

    @Autowired
    private StudentService studentService;

    @Test
    void findStudentByIdShouldFindCorrectStudentById() {
        Student student = new Student(
            UUID.randomUUID(),
            "test",
            "test",
            "test",
            "test",
            null,
            null
        );

        when(
            studentDao.findById(any(UUID.class)))
            .thenReturn(Optional.of(student)
            );
        assertEquals(
            studentService.findStudentById(UUID.randomUUID()),
            student);
    }

    @Test
    void findAllStudentsShouldReturnAllUsersWithRoleStudent() {
        Student student = new Student(
            UUID.randomUUID(),
            "test",
            "test",
            "test",
            "test",
            null,
            null
        );

        when(studentDao.findAll())
            .thenReturn(new ArrayList<>(List.of(student, student)));
        assertEquals(studentService.findAllStudents(), List.of(student, student));
    }

    @Test
    void findStudentsByCourse() {
    }

    @Test
    void findStudentsByGroup() {
    }

    @Test
    void createStudent() {
    }

    @Test
    void deleteStudent() {
    }

    @Test
    void deleteStudentFromGroup() {
    }

    @Test
    void deleteStudentFromCourse() {
    }

    @Test
    void addStudentToCourse() {
    }

    @Test
    void addStudentToGroup() {
    }

    @Test
    void updateStudent() {
    }

    @Test
    void findAllUsersByPage() {
    }
}