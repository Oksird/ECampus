package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(TestConfig.class)
@Transactional
class StudentDaoImplTest {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private GroupDao groupDao;

    @DisplayName("All students are found by course")
    @Test
    void findByCourseShouldReturnAllStudentOnSpecifiedCourse() {
        List<Student> studentsOnCourse = studentDao.findByCourse("Course1");
        int expectedCountOfStudents = 4;
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
                null
            );
        studentDao.create(student);
        studentDao.addToCourse(student.getUserId(), "Course1");
        List<Course> courses =courseDao.findCoursesByUserIdAndUserType(student.getUserId());
        assertTrue(courses.contains(courseDao.findByName("Course1").orElse(null)));

    }

    @DisplayName("Student is removed from course")
    @Test
    void removeFromCourseShouldRemoveSpecificStudentFromSpecificCourse() {
        Student student = studentDao.findAll().get(0);
        studentDao.excludeFromCourse(student.getUserId(), "Course1");
        studentDao.excludeFromCourse(student.getUserId(), "Course2");
        studentDao.excludeFromCourse(student.getUserId(), "Course3");
        assertTrue(courseDao.findCoursesByUserIdAndUserType(student.getUserId()).isEmpty());
    }

    @DisplayName("Student is deleted by id")
    @Test
    void deleteByIdShouldDeleteSpecificStudent() {
        Student student = studentDao.findAll().get(0);
        studentDao.deleteById(student.getUserId());
        assertFalse(studentDao.findAll().contains(student));
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
                    oldStudent.getGroup()
                );
        studentDao.update(oldStudent.getUserId(), newStudent);
        assertEquals(newStudent, studentDao.findById(oldStudent.getUserId()).orElse(null));
    }

    @DisplayName("Pagination")
    @Test
    void findAllByPageShouldReturnAllStudentsOnTheCurrentPage() {
        long pageSize = 4;
        long pageNumber = 2;

        List<Student> studentsOnPage = studentDao.findAll(pageNumber, pageSize);

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
                null
            );
        studentDao.create(student);
        studentDao.addToGroup(student.getUserId(), "AA-01");
        assertNotNull(Objects.requireNonNull(studentDao.findById(student.getUserId()).orElse(null)).getGroup());
    }

    @DisplayName("Student was deleted from group")
    @Test
    void deleteFromGroup() {
        Student student = studentDao.findById(studentDao.findAll().get(0).getUserId()).orElse(null);
        assert student != null;
        studentDao.excludeFromGroup(student.getUserId());
        assertNull(Objects.requireNonNull(studentDao.findById(student.getUserId()).orElse(null)).getGroup());
    }

    @DisplayName("Students was found by group")
    @Test
    void findByGroupShouldReturnAllStudentsOnGroup() {
        List<Student> studentsOnGroup = studentDao.findByGroup("AA-01");
        boolean areOneTheSameGroup = false;
        List<Group> groups = groupDao.findAll();
        for (Student s : studentsOnGroup) {
            areOneTheSameGroup = s.getGroup().equals(groups.iterator().next());
        }
        assertTrue(areOneTheSameGroup);
    }

    @DisplayName("Exception when created student is null")
    @Test
    void createShouldThrowNullPointerException() {
        assertThrows(IllegalArgumentException.class, () -> studentDao.create(null));
    }

    @DisplayName("Student is found by email")
    @Test
    void findByEmailShouldReturnStudentIfEmailIsCorrect() {
        Student expectedStudent = new Student(
            UUID.randomUUID(),
            "Emma",
            "Smith",
            "es1",
            "student123",
            Objects.requireNonNull(groupDao.findByName("AA-01").orElse(null)));
        Student actualStudent = studentDao.findByEmail("es1").orElse(null);
        assert actualStudent != null;
        expectedStudent.setUserId(actualStudent.getUserId());
        assertEquals(expectedStudent, actualStudent);
    }
}
