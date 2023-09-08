package ua.foxminded.muzychenko.university.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.university.TestUniversityApplication;
import ua.foxminded.muzychenko.university.entity.Teacher;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringJUnitConfig(TestUniversityApplication.class)
@Transactional
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @DisplayName("Teachers were found by course")
    @Test
    void findByCourseShouldReturnAllTeachersOnCourse() {
        List<Teacher> teachers = teacherRepository.findByCourses_CourseName("Course1");
        int expectedCountOfTeachers = 4;
        assertEquals(expectedCountOfTeachers, teachers.size());
    }

    @DisplayName("Teacher was updated")
    @Test
    void updateShouldUpdateTeacher() {
        Teacher oldTeacher = teacherRepository.findAll().get(0);
        Teacher newTeacher = new Teacher(
            oldTeacher.getUserId(),
            "a",
            "s",
            "d",
            "f"
        );
        teacherRepository.save(newTeacher);
        oldTeacher = teacherRepository.findById(oldTeacher.getUserId()).orElse(null);
        assertEquals(oldTeacher, newTeacher);
    }

    @DisplayName("Exception when created teacher is null")
    @Test
    void createShouldThrowException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> teacherRepository.save(null));
    }

    @DisplayName("Teacher is found by email")
    @Test
    void findByEmailShouldReturnTeacherIfEmailIsCorrect() {
        Teacher expectedTeacher = new Teacher(
            UUID.randomUUID(),
            "John",
            "Doe",
            "et1",
            "teacher123"
        );
        Teacher actualTeacher = teacherRepository.findByEmail("et1").orElse(null);
        assert actualTeacher != null;
        expectedTeacher.setUserId(actualTeacher.getUserId());
        assertEquals(expectedTeacher, actualTeacher);
    }
}
