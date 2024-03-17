package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Lesson;
import ua.foxminded.muzychenko.entity.LessonTime;
import ua.foxminded.muzychenko.entity.LessonType;
import ua.foxminded.muzychenko.entity.StudyDay;
import ua.foxminded.muzychenko.entity.StudyWeek;
import ua.foxminded.muzychenko.enums.DayOfWeek;
import ua.foxminded.muzychenko.enums.LessonNumber;
import ua.foxminded.muzychenko.enums.TypeOfLesson;
import ua.foxminded.muzychenko.enums.WeekNumber;

import java.time.LocalTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = LessonInfoMapper.class)
public class LessonInfoMapperTest {
    @MockBean
    private CourseInfoMapper courseInfoMapper;
    @MockBean
    private GroupInfoMapper groupInfoMapper;
    @Autowired
    private LessonInfoMapper lessonInfoMapper;

    @Test
    void mapEntityToDTOShouldMapLessonToLessonInfo() {

        LessonType lessonType = new LessonType(
            UUID.randomUUID(),
            TypeOfLesson.LECTURE
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

        StudyDay studyDay = new StudyDay(
            UUID.randomUUID(),
            DayOfWeek.MONDAY
        );

        StudyWeek studyWeek = new StudyWeek(
            UUID.randomUUID(),
            WeekNumber.FIRST
        );

        LessonTime lessonTime = new LessonTime(
            UUID.randomUUID(),
            LessonNumber.FIRST,
            LocalTime.now()
        );

        Lesson lesson = new Lesson(
            UUID.randomUUID(),
            lessonType,
            course,
            group,
            studyDay,
            studyWeek,
            lessonTime,
            "info"
        );

        CourseInfo courseInfo = new CourseInfo(
            course.getCourseId().toString(),
            course.getCourseName(),
            course.getCourseDescription(),
            null
        );

        GroupInfo groupInfo = new GroupInfo(
            group.getGroupId().toString(),
            group.getGroupName(),
            0,
            Collections.singletonList(courseInfo)
        );

        LessonInfo lessonInfo = new LessonInfo(
            lesson.getId().toString(),
            lesson.getLessonType().getType(),
            courseInfo,
            groupInfo,
            studyDay.getDayOfWeek(),
            studyWeek.getWeekNumber(),
            lessonTime.getLessonNumber(),
            lesson.getAdditionalInfo()
        );

        when(groupInfoMapper.mapGroupEntityToGroupInfo(any(Group.class)))
            .thenReturn(groupInfo);
        when(courseInfoMapper.mapCourseEntityToCourseInfo(any(Course.class)))
            .thenReturn(courseInfo);

        assertEquals(lessonInfo, lessonInfoMapper.mapEntityToDTO(lesson));
    }
}
