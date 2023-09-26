package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TeacherProfileMapper.class)
class TeacherProfileMapperTest {

    @Autowired
    private TeacherProfileMapper mapper;

    @Test
    void mapTeacherEntityToProfile() {

        Course course = new Course(
            UUID.randomUUID(),
            "cn",
            "cd"
        );

        CourseInfo courseInfo = new CourseInfo(
            course.getCourseId().toString(),
            course.getCourseName(),
            course.getCourseDescription()
        );

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass"
        );

        TeacherProfile expectedTeacherProfile = new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            new HashSet<>(Collections.singleton(courseInfo))
        );

        assertEquals(expectedTeacherProfile, mapper.mapTeacherEntityToProfile(teacher, new HashSet<>(Collections.singleton(course))));
    }
}