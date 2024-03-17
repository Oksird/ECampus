package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.LessonCreationDto;
import ua.foxminded.muzychenko.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.dto.profile.Schedule;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Lesson;
import ua.foxminded.muzychenko.entity.LessonTime;
import ua.foxminded.muzychenko.entity.LessonType;
import ua.foxminded.muzychenko.entity.StudyDay;
import ua.foxminded.muzychenko.entity.StudyWeek;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.enums.DayOfWeek;
import ua.foxminded.muzychenko.enums.LessonNumber;
import ua.foxminded.muzychenko.enums.TypeOfLesson;
import ua.foxminded.muzychenko.enums.WeekNumber;
import ua.foxminded.muzychenko.repository.CourseRepository;
import ua.foxminded.muzychenko.repository.GroupRepository;
import ua.foxminded.muzychenko.repository.LessonRepository;
import ua.foxminded.muzychenko.repository.LessonTimeRepository;
import ua.foxminded.muzychenko.repository.LessonTypeRepository;
import ua.foxminded.muzychenko.repository.StudyDayRepository;
import ua.foxminded.muzychenko.repository.StudyWeekRepository;
import ua.foxminded.muzychenko.repository.TeacherRepository;
import ua.foxminded.muzychenko.service.mapper.LessonInfoMapper;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ScheduleService.class)
public class ScheduleServiceTest {

    @MockBean
    private LessonRepository lessonRepository;
    @MockBean
    private GroupRepository groupRepository;
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private StudyDayRepository studyDayRepository;
    @MockBean
    private StudyWeekRepository studyWeekRepository;
    @MockBean
    private LessonTypeRepository lessonTypeRepository;
    @MockBean
    private LessonTimeRepository lessonTimeRepository;
    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private LessonInfoMapper lessonInfoMapper;
    @Autowired
    private ScheduleService scheduleService;

    private Course course;
    private Group group;
    private StudyWeek studyWeek;
    private StudyDay studyDay;
    private LessonTime lessonTime;
    private LessonType lessonType;
    private Lesson lesson;
    private Teacher teacher;
    private TeacherProfile teacherProfile;
    private CourseInfo courseInfo;
    private GroupInfo groupInfo;
    private LessonInfo lessonInfo;
    private LessonCreationDto lessonCreationDto;

    @BeforeEach
    public void setUp() {
        course = new Course(UUID.randomUUID(), "Math", "numbers");
        group = new Group(UUID.randomUUID(), "AA-01");
        studyWeek = new StudyWeek(UUID.randomUUID(), WeekNumber.FIRST);
        studyDay = new StudyDay(UUID.randomUUID(), DayOfWeek.MONDAY);
        lessonTime = new LessonTime(UUID.randomUUID(), LessonNumber.FIRST, LocalTime.now());
        lessonType = new LessonType(UUID.randomUUID(), TypeOfLesson.LECTURE);
        lesson = new Lesson(UUID.randomUUID(), lessonType, course, group, studyDay, studyWeek, lessonTime, "info");
        teacher = new Teacher(UUID.randomUUID(), "fn", "ln", "em", "pas", "pn", "adr");
        teacherProfile = new TeacherProfile(UUID.randomUUID().toString(), teacher.getFirstName(), teacher.getLastName(), teacher.getEmail(), teacher.getPhoneNumber(), teacher.getAddress());
        courseInfo = new CourseInfo(course.getCourseId().toString(), course.getCourseName(), course.getCourseDescription(), teacherProfile);
        groupInfo = new GroupInfo(group.getGroupId().toString(), group.getGroupName(), 0, Collections.singletonList(courseInfo));
        lessonInfo = new LessonInfo(lesson.getId().toString(), lessonType.getType(), courseInfo, groupInfo, studyDay.getDayOfWeek(), studyWeek.getWeekNumber(), lessonTime.getLessonNumber(), "info");
        lessonCreationDto= new LessonCreationDto(
            WeekNumber.FIRST,
            DayOfWeek.MONDAY,
            LessonNumber.FIRST,
            TypeOfLesson.LECTURE,
            "AA-01",
            "Math",
            "info"
        );
    }

    @Test
    void createLessonShouldCreateLessonEntity() {

        when(studyWeekRepository.findStudyWeekByWeekNumber(any(WeekNumber.class)))
            .thenReturn(Optional.of(studyWeek));
        when(studyDayRepository.findStudyDayByDayOfWeek(any(DayOfWeek.class)))
            .thenReturn(Optional.of(studyDay));
        when(lessonTimeRepository.findLessonTimeByLessonNumber(any(LessonNumber.class)))
            .thenReturn(Optional.of(lessonTime));
        when(lessonTypeRepository.findLessonTypeByType(any(TypeOfLesson.class)))
            .thenReturn(Optional.of(lessonType));
        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.of(group));
        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(course));

        scheduleService.createLesson(lessonCreationDto);

        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void getScheduleForGroupShouldReturnListOfLessonsForSpecificGroup() {

        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.of(group));
        when(lessonRepository.findLessonsByGroup(any(Group.class)))
            .thenReturn(Collections.singletonList(lesson));
        when(lessonInfoMapper.mapEntityToDTO(any(Lesson.class)))
            .thenReturn(lessonInfo);

        Schedule schedule = new Schedule(mapLessonToSchedule(lesson));

        assertEquals(schedule, scheduleService.getScheduleForGroup("AA-01"));
    }

    @Test
    void getScheduleForTeacherShouldReturnScheduleForSpecificTeacher() {
        teacher.setCourse(course);
        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));
        when(lessonRepository.findLessonsByCourse(any(Course.class)))
            .thenReturn(Collections.singletonList(lesson));

        Schedule schedule = new Schedule(mapLessonToSchedule(lesson));

        assertEquals(schedule, scheduleService.getScheduleForTeacher("email"));
    }

    @Test
    void editLessonShouldSaveEditedLessonEntity() {
        LessonType lessonTypeForEdit = new LessonType(
            UUID.randomUUID(),
            TypeOfLesson.SEMINAR
        );

        when(studyWeekRepository.findStudyWeekByWeekNumber(any())).thenReturn(Optional.of(studyWeek));
        when(studyDayRepository.findStudyDayByDayOfWeek(any())).thenReturn(Optional.of(studyDay));
        when(lessonTimeRepository.findLessonTimeByLessonNumber(any())).thenReturn(Optional.of(lessonTime));
        when(lessonRepository.findByStudyWeekAndStudyDayAndLessonTime(any(), any(), any())).thenReturn(Optional.of(lesson));
        when(courseRepository.findByCourseName(any())).thenReturn(Optional.of(course));
        when(groupRepository.findByGroupName(any())).thenReturn(Optional.of(group));
        when(lessonTypeRepository.findLessonTypeByType(any())).thenReturn(Optional.of(lessonTypeForEdit));
        scheduleService.editLesson(lessonCreationDto);

        Optional<Lesson> editedLessonOptional = lessonRepository.findByStudyWeekAndStudyDayAndLessonTime(studyWeek, studyDay, lessonTime);
        assertTrue(editedLessonOptional.isPresent());

        Lesson editedLesson = editedLessonOptional.get();
        assertEquals(lessonCreationDto.getCourseName(), editedLesson.getCourse().getCourseName());
        assertEquals(lessonCreationDto.getGroupName(), editedLesson.getGroup().getGroupName());
        assertEquals(lessonCreationDto.getAdditionalInfo(), editedLesson.getAdditionalInfo());
        assertEquals(lessonTypeForEdit.getType(), editedLesson.getLessonType().getType());
    }

    private Map<WeekNumber, Map<DayOfWeek, List<LessonInfo>>> mapLessonToSchedule(Lesson lesson) {
        return Stream.of(lesson)
            .collect(Collectors.groupingBy(
                    lesson1 -> lesson1.getStudyWeek().getWeekNumber(),
                Collectors.groupingBy(
                        lesson1 -> lesson1.getStudyDay().getDayOfWeek(),
                    Collectors.mapping(
                        lessonInfoMapper::mapEntityToDTO,
                        Collectors.toList()
                    )
                )
            ));
    }

}
