package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CourseInfoMapper.class, TeacherProfileMapper.class})
class CourseInfoMapperTest {

    @Autowired
    private CourseInfoMapper mapper;
    @Autowired
    private TeacherProfileMapper teacherProfileMapper;

    @Test
    void mapCourseEntityToCourseInfoShouldReturnCourseInfoBasedOnCourseEntity() {
        Course course = new Course(
            UUID.randomUUID(),
            "cn",
            "cd"
        );

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "name",
            "surname",
            "email",
            "pass",
            "380228883399",
            "address"
        );

        course.setTeacher(teacher);

        CourseInfo expectedCourseInfo = new CourseInfo(
            course.getCourseId().toString(),
            course.getCourseName(),
            course.getCourseDescription(),
            teacherProfileMapper.mapTeacherEntityToProfile(course.getTeacher())
        );

        assertEquals(expectedCourseInfo, mapper.mapCourseEntityToCourseInfo(course));
    }
}
