package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.TeacherDao;
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(TestConfig.class)
@Transactional
class TeacherDaoImplTest {

    @Autowired
    private TeacherDao teacherDao;

    @DisplayName("Teacher added to course")
    @Test
    void addToCourseShouldAddTeacherToCourse() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "a",
            "s",
            "d",
            "f",
            null
        );
        teacherDao.create(teacher);
        teacherDao.addToCourse(teacher.getUserId(), "Course1");
        assertNotNull(teacherDao.findById(teacher.getUserId()));
    }

    @DisplayName("Teachers were found by course")
    @Test
    void findByCourseShouldReturnAllTeachersOnCourse() {
        List<Teacher> teachers = teacherDao.findByCourse("Course1");
        int expectedCountOfTeachers = 4;
        assertEquals(expectedCountOfTeachers, teachers.size());
    }

    @DisplayName("Teacher was deleted")
    @Test
    void deleteFromCourseShouldDeleteTeacherFromCourse() {
        Teacher teacher = teacherDao.findByCourse("Course1").get(0);
        teacherDao.deleteFromCourse(teacher.getUserId(), "Course1");
        assertNull(Objects.requireNonNull(teacherDao.findById(teacher.getUserId()).orElse(null)).getCourseId());
    }

    @DisplayName("Teacher was updated")
    @Test
    void updateShouldUpdateTeacher() {
        Teacher oldTeacher = teacherDao.findAll().get(0);
        Teacher newTeacher = new Teacher(
            oldTeacher.getUserId(),
            "a",
            "s",
            "d",
            "f",
            oldTeacher.getCourseId()
        );
        teacherDao.update(oldTeacher.getUserId(), newTeacher);
        oldTeacher = teacherDao.findById(oldTeacher.getUserId()).orElse(null);
        assertEquals(oldTeacher, newTeacher);
    }

    @DisplayName("Exception when created teacher is null")
    @Test
    void createShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> teacherDao.create(null));
    }
}
