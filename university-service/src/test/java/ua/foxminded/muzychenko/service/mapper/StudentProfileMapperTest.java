package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = StudentProfileMapper.class)
class StudentProfileMapperTest {

    @Autowired
    private StudentProfileMapper mapper;

    @Test
    void mapStudentInfoToProfileShouldReturnStudentProfileBasedOnStudentEntity() {

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

        Group group = new Group(
            UUID.randomUUID(),
            "gn"
        );

        GroupInfo groupInfo = new GroupInfo(
            group.getGroupId().toString(),
            group.getGroupName(),
            0
        );

        Student student = new Student(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            group
        );

        StudentProfile expectedStudentProfile = new StudentProfile(
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            groupInfo,
            new HashSet<>(Collections.singleton(courseInfo))
        );

        assertEquals(
            expectedStudentProfile ,
            mapper.mapStudentInfoToProfile(student, group, new HashSet<>(Collections.singleton(course))));
    }

    @Test
    void mapStudentInfoToProfileShouldReturnStudentProfileBasedOnStudentEntityWithoutGroupInfo() {

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

        Student student = new Student(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            null
        );

        StudentProfile expectedStudentProfile = new StudentProfile(
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            null,
            new HashSet<>(Collections.singleton(courseInfo))
        );

        assertEquals(
            expectedStudentProfile ,
            mapper.mapStudentInfoToProfile(student, null, new HashSet<>(Collections.singleton(course))));
    }
}