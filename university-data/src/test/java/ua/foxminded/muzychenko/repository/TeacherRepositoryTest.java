package ua.foxminded.muzychenko.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.exception.UserNotFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = TeacherRepository.class)
@Import(DataTestConfig.class)
@Transactional
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;

    @DisplayName("Teacher is found by email")
    @Test
    void findByEmailShouldReturnTeacherIfEmailIsCorrect() {
        String email = "jemail@mail.com";

        Teacher expectedTeacher = new Teacher(
            UUID.randomUUID(),
            "Liza",
            "Bored",
            email,
            "password7",
            "380786489755",
            "Ikea 8"
        );

        expectedTeacher.setCourse(courseRepository.findByCourseName("Course3").orElseThrow(CourseNotFoundException::new));

        Teacher actualTeacher = teacherRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        expectedTeacher.setUserId(actualTeacher.getUserId());
        assertEquals(expectedTeacher, actualTeacher);
    }
}
