package ua.foxminded.muzychenko.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Lesson;
import ua.foxminded.muzychenko.entity.LessonTime;
import ua.foxminded.muzychenko.entity.StudyDay;
import ua.foxminded.muzychenko.entity.StudyWeek;
import ua.foxminded.muzychenko.enums.DayOfWeek;
import ua.foxminded.muzychenko.enums.LessonNumber;
import ua.foxminded.muzychenko.enums.WeekNumber;
import ua.foxminded.muzychenko.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.exception.GroupNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = LessonRepository.class)
@Import(DataTestConfig.class)
@Transactional
public class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudyWeekRepository studyWeekRepository;
    @Autowired
    private StudyDayRepository studyDayRepository;
    @Autowired
    private LessonTimeRepository lessonTimeRepository;

    @Test
    void findLessonsByGroupShouldReturnLessonsForGroup() {
        Group group = groupRepository.findByGroupName("AA-01").orElseThrow(GroupNotFoundException::new);

        List<Lesson> expectedLessons = lessonRepository.findAll()
            .stream()
            .filter(lesson -> lesson.getGroup().equals(group))
            .toList();

        List<Lesson> actualLessons = lessonRepository.findLessonsByGroup(group);

        assertEquals(expectedLessons, actualLessons);
    }

    @Test
    void findLessonsByCourseShouldReturnLessonsRelatedToCorrectCourse() {
        Course course = courseRepository.findByCourseName("Course1").orElseThrow(CourseNotFoundException::new);

        List<Lesson> expectedLessons = lessonRepository.findAll()
            .stream()
            .filter(lesson -> lesson.getCourse().equals(course))
            .toList();

        List<Lesson> actualLessons = lessonRepository.findLessonsByCourse(course);

        assertEquals(expectedLessons, actualLessons);
    }

    @Test
    void findByStudyWeekAndStudyDayAndLessonTimeShouldReturnLessonWithCorrectTime() {
        StudyWeek studyWeek = studyWeekRepository.findStudyWeekByWeekNumber(WeekNumber.FIRST)
            .orElseThrow(EntityNotFoundException::new);
        StudyDay studyDay = studyDayRepository.findStudyDayByDayOfWeek(DayOfWeek.MONDAY)
            .orElseThrow(EntityNotFoundException::new);
        LessonTime lessonTime = lessonTimeRepository.findLessonTimeByLessonNumber(LessonNumber.SECOND)
            .orElseThrow(EntityNotFoundException::new);

        Optional<Lesson> expectedLesson = lessonRepository.findAll()
            .stream()
            .filter(lesson ->
                lesson.getStudyWeek().equals(studyWeek) &&
                    lesson.getStudyDay().equals(studyDay) &&
                    lesson.getLessonTime().equals(lessonTime)
            )
            .findFirst();

        Optional<Lesson> actualLesson = lessonRepository.findByStudyWeekAndStudyDayAndLessonTime(studyWeek, studyDay, lessonTime)
            .stream()
            .findFirst();

        assertEquals(expectedLesson, actualLesson);
    }
}
