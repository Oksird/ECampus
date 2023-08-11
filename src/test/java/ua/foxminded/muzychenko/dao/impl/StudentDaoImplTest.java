package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.Student;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(TestConfig.class)
@Transactional
class StudentDaoImplTest {

    @Autowired
    private StudentDao studentDao;

    @DisplayName("All students are found by course")
    @Test
    void findByCourseShouldReturnAllStudentOnSpecifiedCourse() {
        List<Student> studentsOnCourse = studentDao.findByCourse("Course1");
        int expectedCountOfStudents = 4;
        System.out.println(studentDao.findAll());
        assertEquals(expectedCountOfStudents, studentsOnCourse.size());
    }

    @DisplayName("Course doesn't exist")
    @Test
    void findByCourseShouldReturnEmptyListIfCourseDoesntExist() {
        assertTrue(studentDao.findByCourse("").isEmpty());
    }

    @DisplayName("Student is added to course")
    @Test
    void addToCourseShouldAddSpecificStudentToSpecificCourse() {
        Student student =
            new Student(
                UUID.randomUUID(),
                "name",
                "surname",
                "e@g.com",
                "pass111",
                null,
                null
            );
        studentDao.create(student);
        studentDao.addToCourse(student.getUserId(), "Course1");
        UUID courseId = Objects.requireNonNull(studentDao.findById(student.getUserId()).orElse(null)).getCourseId();
        assertNotNull(courseId);
    }

    @DisplayName("Student is removed from course")
    @Test
    void removeFromCourseShouldRemoveSpecificStudentFromSpecificCourse() {
        UUID idOfStudent = studentDao.findAll().get(0).getUserId();
        studentDao.deleteFromCourse(idOfStudent, "Course1");
        studentDao.deleteFromCourse(idOfStudent, "Course2");
        studentDao.deleteFromCourse(idOfStudent, "Course3");
        assertNull(Objects.requireNonNull(studentDao.findById(idOfStudent).orElse(null)).getCourseId());
    }

    @DisplayName("Student is deleted by id")
    @Test
    void deleteByIdShouldDeleteSpecificStudent() {
        Optional<Student> expectedStudent;
        UUID studentId = studentDao.findAll().get(0).getCourseId();
        studentDao.deleteById(studentId);
        expectedStudent = studentDao.findById(studentId);
        assertTrue(expectedStudent.isEmpty());
    }

    @DisplayName("Student is updated correctly")
    @Test
    void updateShouldReplaceOldStudentEntityWithNewOne() {
        Student oldStudent = studentDao.findById(studentDao.findAll().get(0).getUserId()).orElse(null);
        assert oldStudent != null;
        Student newStudent = new Student(
                    oldStudent.getUserId(),
                    "test",
                    "test",
                    "test",
                    "test",
                    oldStudent.getGroupId(),
                    oldStudent.getCourseId()
                );
        studentDao.update(oldStudent.getUserId(), newStudent);
        assertEquals(newStudent, studentDao.findById(oldStudent.getUserId()).orElse(null));
    }

    @DisplayName("Pagination")
    @Test
    void findAllByPageShouldReturnAllStudentsOnTheCurrentPage() {
        long pageSize = 4;
        long pageNumber = 2;

        List<Student> studentsOnPage = studentDao.findAllByPage(pageNumber, pageSize);

        List<Student> allStudents = studentDao.findAll();
        int startIndex = (int) (pageSize * (pageNumber - 1));
        int endIndex = (int) (pageSize * pageNumber);
        if (startIndex >= allStudents.size()) {
            assertTrue(studentsOnPage.isEmpty());
        } else {
            List<Student> expectedStudents = allStudents.subList(startIndex, Math.min(endIndex, allStudents.size()));
            assertEquals(expectedStudents, studentsOnPage);
        }
    }

    @DisplayName("Student was added to group")
    @Test
    void addToGroupShouldAddStudentToGroup() {
        Student student =
            new Student(
                UUID.randomUUID(),
                "name",
                "lastName",
                "email",
                "pass",
                null,
                null
            );
        studentDao.create(student);
        studentDao.addToGroup(student.getUserId(), "AA-01");
        assertNotNull(Objects.requireNonNull(studentDao.findById(student.getUserId()).orElse(null)).getGroupId());
    }

    @DisplayName("Student was deleted from group")
    @Test
    void deleteFromGroup() {
        Student student = studentDao.findById(studentDao.findAll().get(0).getUserId()).orElse(null);
        assert student != null;
        studentDao.deleteFromGroup(student.getUserId(), "AA-01");
        assertNull(Objects.requireNonNull(studentDao.findById(student.getUserId()).orElse(null)).getGroupId());
    }

    @DisplayName("Students was found by group")
    @Test
    void findByGroupShouldReturnAllStudentsOnGroup() {
        List<Student> studentsOnGroup = studentDao.findByGroup("AA-01");
        boolean areOneTheSameGroup = false;
        UUID groupId = studentsOnGroup.get(0).getGroupId();
        for (Student s : studentsOnGroup) {
            areOneTheSameGroup = s.getGroupId().equals(groupId);
        }
        assertTrue(areOneTheSameGroup);
    }

    @DisplayName("Exception when created student is null")
    @Test
    void createShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> studentDao.create(null));
    }
}
