package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {TeacherProfileMapper.class, CourseInfoMapper.class})
class TeacherProfileMapperTest {

    @Autowired
    private TeacherProfileMapper mapper;
    @Autowired
    private CourseInfoMapper courseInfoMapper;

    @Test
    void mapTeacherEntityToProfile() {
        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "380267773322",
            "address"
        );

        Course course = new Course(
            UUID.randomUUID(),
            "cn",
            "cd"
        );

        course.setTeacher(teacher);

        CourseInfo courseInfo = courseInfoMapper.mapCourseEntityToCourseInfo(course);

        TeacherProfile expectedTeacherProfile = new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            teacher.getPhoneNumber(),
            teacher.getAddress()
        );

        assertEquals(expectedTeacherProfile, mapper.mapTeacherEntityToProfile(teacher));
    }
}
