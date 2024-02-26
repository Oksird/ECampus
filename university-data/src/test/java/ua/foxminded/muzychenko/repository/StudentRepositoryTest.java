package ua.foxminded.muzychenko.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;
import ua.foxminded.muzychenko.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = StudentRepository.class)
@Import(DataTestConfig.class)
@Transactional
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;


    @DisplayName("Students were found by group")
    @Test
    void findByGroupShouldReturnAllStudentsByGroup() {
        List<Student> studentsOnGroup = studentRepository.findByGroup_GroupName("AA-01");

        boolean areOneTheSameGroup = false;

        List<Group> studentsGroups = new ArrayList<>();
        studentsOnGroup.forEach(student -> studentsGroups.add(student.getGroup()));



        for (Student s : studentsOnGroup) {
            areOneTheSameGroup = s.getGroup().equals(studentsGroups.iterator().next());
        }
        assertTrue(areOneTheSameGroup);
    }

    @DisplayName("Student is found by email")
    @Test
    void findByEmailShouldReturnStudentIfEmailIsCorrect() {
        String email = "james.m@mail.com";

        Student expectedStudent = new Student(
            UUID.randomUUID(),
            "James",
            "Oloven",
            email,
            "password2",
            groupRepository.findByGroupName("AA-01").orElse(null),
            "380528694023",
            "Pokash 4"
        );
        Student actualStudent = studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        expectedStudent.setUserId(actualStudent.getUserId());
        assertEquals(expectedStudent, actualStudent);
    }
}
