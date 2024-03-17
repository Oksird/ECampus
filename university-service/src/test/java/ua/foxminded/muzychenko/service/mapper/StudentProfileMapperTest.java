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
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {StudentProfileMapper.class, CourseInfoMapper.class, GroupInfoMapper.class})
class StudentProfileMapperTest {

    @Autowired
    private StudentProfileMapper studentProfileMapper;
    @Autowired
    private CourseInfoMapper courseInfoMapper;
    @Autowired
    private GroupInfoMapper groupInfoMapper;

    @Test
    void mapStudentInfoToProfileShouldReturnStudentProfileBasedOnStudentEntity() {

        Course course = new Course(
            UUID.randomUUID(),
            "cn",
            "cd"
        );

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "380228883399",
            "address"
        );

        course.setTeacher(teacher);

        CourseInfo courseInfo = courseInfoMapper.mapCourseEntityToCourseInfo(course);

        Group group = new Group(
            UUID.randomUUID(),
            "gn"
        );

        GroupInfo groupInfo = groupInfoMapper.mapGroupEntityToGroupInfo(group);

        Student student = new Student(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            group,
            "380273827733",
            "address"
        );

        StudentProfile expectedStudentProfile = new StudentProfile(
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            groupInfo,
            student.getPhoneNumber(),
            student.getAddress()
        );

        assertEquals(
            expectedStudentProfile ,
            studentProfileMapper.mapStudentInfoToProfile(student));
    }

    @Test
    void mapStudentInfoToProfileShouldReturnStudentProfileBasedOnStudentEntityWithoutGroupInfo() {

        Course course = new Course(
            UUID.randomUUID(),
            "cn",
            "cd"
        );

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "380228883399",
            "address"
        );

        course.setTeacher(teacher);

        CourseInfo courseInfo = courseInfoMapper.mapCourseEntityToCourseInfo(course);

        Student student = new Student(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            null,
            "380672883366",
            "address"
        );

        StudentProfile expectedStudentProfile = new StudentProfile(
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            null,
            student.getPhoneNumber(),
            student.getAddress()
        );

        assertEquals(
            expectedStudentProfile ,
            studentProfileMapper.mapStudentInfoToProfile(student));
    }
}
