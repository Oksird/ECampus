package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.entity.Course;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CourseInfoMapper.class)
class CourseInfoMapperTest {

    @Autowired
    private CourseInfoMapper mapper;

    @Test
    void mapCourseEntityToCourseInfoShouldReturnCourseInfoBasedOnCourseEntity() {
        Course course = new Course(
            UUID.randomUUID(),
            "cn",
            "cd"
        );

        CourseInfo expectedCourseInfo = new CourseInfo(
            course.getCourseId().toString(),
            course.getCourseName(),
            course.getCourseDescription()
        );

        assertEquals(expectedCourseInfo, mapper.mapCourseEntityToCourseInfo(course));
    }
}