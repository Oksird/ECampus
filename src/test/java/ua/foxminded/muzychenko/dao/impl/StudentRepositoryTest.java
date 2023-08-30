package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.GroupRepository;
import ua.foxminded.muzychenko.dao.StudentRepository;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(TestConfig.class)
@Transactional
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;

    @DisplayName("All students are found by course")
    @Test
    void findByCourseShouldReturnAllStudentOnSpecifiedCourse() {
        List<Student> studentsOnCourse = studentRepository.findByCourses_CourseName("Course1");
        int expectedCountOfStudents = 4;
        assertEquals(expectedCountOfStudents, studentsOnCourse.size());
    }

    @DisplayName("Course doesn't exist")
    @Test
    void findByCourseShouldReturnEmptyListIfCourseDoesntExist() {
        assertTrue(studentRepository.findByCourses_CourseName("").isEmpty());
    }

    @DisplayName("Student is deleted by id")
    @Test
    void deleteByIdShouldDeleteSpecificStudent() {
        Student student = studentRepository.findAll().get(0);
        studentRepository.deleteById(student.getUserId());
        assertFalse(studentRepository.findAll().contains(student));
    }

    @DisplayName("Student is updated correctly")
    @Test
    void updateShouldReplaceOldStudentEntityWithNewOne() {
        Student oldStudent = studentRepository.findById(studentRepository.findAll().get(0).getUserId()).orElse(null);
        assert oldStudent != null;
        Student newStudent = new Student(
                    oldStudent.getUserId(),
                    "test",
                    "test",
                    "test",
                    "test",
                    oldStudent.getGroup()
                );
        studentRepository.save(newStudent);
        assertEquals(newStudent, studentRepository.findById(oldStudent.getUserId()).orElse(null));
    }

    @DisplayName("Pagination")
    @Test
    void findAllByPageShouldReturnAllStudentsOnTheCurrentPage() {
        int pageSize = 5;
        int pageNumber = 0;

        List<Student> studentsOnPage = studentRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();

        List<Student> expectedStudents = studentRepository.findAll().subList(0, 5);

        assertTrue(expectedStudents.containsAll(studentsOnPage));
    }

    @DisplayName("Students was found by group")
    @Test
    void findByGroupShouldReturnAllStudentsOnGroup() {
        List<Student> studentsOnGroup = studentRepository.findByGroup_GroupName("AA-01");
        boolean areOneTheSameGroup = false;
        List<Group> groups = groupRepository.findAll();
        for (Student s : studentsOnGroup) {
            areOneTheSameGroup = s.getGroup().equals(groups.iterator().next());
        }
        assertTrue(areOneTheSameGroup);
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
            Objects.requireNonNull(groupRepository.findByGroupName("AA-01").orElse(null)));
        Student actualStudent = studentRepository.findByEmail("es1").orElse(null);
        assert actualStudent != null;
        expectedStudent.setUserId(actualStudent.getUserId());
        assertEquals(expectedStudent, actualStudent);
    }
}
