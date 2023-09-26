package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.muzychenko.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Lesson;
import ua.foxminded.muzychenko.entity.Teacher;

import java.sql.Date;
import java.sql.Time;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = LessonInfoMapper.class)
class LessonInfoMapperTest {

    @MockBean
    private TeacherProfileMapper teacherMapper;
    @Autowired
    private LessonInfoMapper mapper;

    @Test
    void mapLessonEntityToLessonInfoShouldReturnLessonInfoBasedOnLessonEntity() {

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass"
        );

        Course course = new Course(
            UUID.randomUUID(),
            "cn",
            "cd"
        );

        Group group = new Group(
            UUID.randomUUID(),
            "gn"
        );

        Lesson lesson = new Lesson(
            UUID.randomUUID(),
            course,
            group,
            teacher,
            Date.valueOf("2022-01-01"),
            Time.valueOf("08:30:00"),
            Time.valueOf("09:30:00")
        );

        TeacherProfile teacherProfile = new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            new HashSet<>()
        );

        when(teacherMapper.mapTeacherEntityToProfile(any(Teacher.class), anySet()))
            .thenReturn(teacherProfile);

        LessonInfo expectedLessonInfo = new LessonInfo(
            lesson.getLessonId().toString(),
            lesson.getCourse().getCourseName(),
            teacherMapper.mapTeacherEntityToProfile(teacher, new HashSet<>()),
            lesson.getGroup().getGroupName(),
            lesson.getDate(),
            lesson.getStartTime(),
            lesson.getEndTime()
        );

        assertEquals(expectedLessonInfo, mapper.mapLessonEntityToLessonInfo(lesson));
    }
}