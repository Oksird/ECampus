package ua.foxminded.muzychenko.university.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.university.TestUniversityApplication;
import ua.foxminded.muzychenko.university.dao.CourseRepository;
import ua.foxminded.muzychenko.university.dao.GroupRepository;
import ua.foxminded.muzychenko.university.dao.LessonRepository;
import ua.foxminded.muzychenko.university.dao.StudentRepository;
import ua.foxminded.muzychenko.university.dao.TeacherRepository;
import ua.foxminded.muzychenko.university.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.university.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.university.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.university.entity.Course;
import ua.foxminded.muzychenko.university.entity.Group;
import ua.foxminded.muzychenko.university.entity.Lesson;
import ua.foxminded.muzychenko.university.entity.Student;
import ua.foxminded.muzychenko.university.entity.Teacher;
import ua.foxminded.muzychenko.university.service.mapper.LessonInfoMapper;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(TestUniversityApplication.class)
class LessonServiceTest {
    @Autowired
    private LessonService lessonService;
    @MockBean
    private LessonRepository lessonRepository;
    @MockBean
    private GroupRepository groupRepository;
    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private LessonInfoMapper lessonInfoMapper;
    private Course course;
    private Group group;
    private Teacher teacher;
    private Lesson lesson;
    private LessonInfo lessonInfo;

    @BeforeEach
    void setUp() {
        course = new Course(UUID.randomUUID(), "Test course", "Test test test");
        group = new Group(UUID.randomUUID(), "TT-99");
        teacher = new Teacher(UUID.randomUUID(), "tName", "tLName", "email@test.t", "tPass");
        lesson = new Lesson(
            UUID.randomUUID(),
            course,
            group,
            teacher,
            Date.valueOf("2022-01-01"),
            Time.valueOf("08:30:00"),
            Time.valueOf("09:30:00")
        );

        TeacherProfile teacherProfile = new TeacherProfile(
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            new HashSet<>(List.of(new CourseInfo(course.getCourseName(), course.getCourseDescription())))
        );

        lessonInfo = new LessonInfo(
            course.getCourseName(),
            teacherProfile,
            group.getGroupName(),
            lesson.getDate(),
            lesson.getStartTime(),
            lesson.getEndTime()
        );
    }

    @Test
    void createLessonShouldCreateNewLessonInDB() {
        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(course));

        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.of(group));

        when(teacherRepository.findByEmail(any(String.class))).
            thenReturn(Optional.of(teacher));

        when(lessonRepository.save(any(Lesson.class)))
            .thenReturn(lesson);

        lessonService.createLesson(lessonInfo);

        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void findLessonByIdShouldFindLessonWithEnteredId() {
        when(lessonRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(lesson));

        when(lessonInfoMapper.mapLessonEntityToLessonInfo(any(Lesson.class)))
            .thenReturn(lessonInfo);

        assertEquals(
            lessonInfo,
            lessonService.findLessonById(UUID.randomUUID())
        );
    }

    @Test
    void findAllLessonsShouldFindAllLessons() {
        when(lessonRepository.findAll())
            .thenReturn(new ArrayList<>(List.of(lesson)));

        when(lessonInfoMapper.mapLessonEntityToLessonInfo(any(Lesson.class)))
            .thenReturn(lessonInfo);

        assertEquals(
            new ArrayList<>(Collections.singletonList(lessonInfo)),
            lessonService.findAllLessons()
        );
    }

    @Test
    void findAllLessonsShouldReturnPageOfCourseInfos() {
        when(lessonRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(Collections.singletonList(lesson)));

        when(lessonInfoMapper.mapLessonEntityToLessonInfo(any(Lesson.class)))
            .thenReturn(lessonInfo);

        assertEquals(
            new ArrayList<>(Collections.singletonList(lessonInfo)),
            lessonService.findAllLessons(1, 1)
        );
    }

    @Test
    void findLessonByDateShouldReturnAllCoursesWithEnteredDate() {
        when(lessonRepository.findByDate(any(Date.class)))
            .thenReturn(new ArrayList<>(Collections.singletonList(lesson)));
        when(lessonInfoMapper.mapLessonEntityToLessonInfo(any(Lesson.class)))
            .thenReturn(lessonInfo);

        assertEquals(
            new ArrayList<>(Collections.singletonList(lessonInfo)),
            lessonService.findLessonByDate(Date.valueOf("2022-01-01"))
        );
    }

    @Test
    void findTeacherLessonsShouldAllTeacherLessons() {
        when(lessonRepository.findByTeacherEmail(any(String.class)))
            .thenReturn(Collections.singletonList(lesson));

        when(lessonInfoMapper.mapLessonEntityToLessonInfo(any(Lesson.class)))
            .thenReturn(lessonInfo);

        assertEquals(
            Collections.singletonList(lessonInfo),
            lessonService.findTeacherLessons("teacher@email.com")
            );
    }

    @Test
    void findLessonByGroupNameShouldReturnAllGroupLessons() {
        when(lessonRepository.findByGroupGroupName(any(String.class)))
            .thenReturn(Collections.singletonList(lesson));

        when(lessonInfoMapper.mapLessonEntityToLessonInfo(any(Lesson.class)))
            .thenReturn(lessonInfo);

        assertEquals(
            Collections.singletonList(lessonInfo),
            lessonService.findLessonByGroupName("groupName")
        );
    }

    @Test
    void findLessonByCourseNameShouldReturnLessonsOfEnteredCourse() {
        when(lessonRepository.findByCourseCourseName(any(String.class)))
            .thenReturn(Collections.singletonList(lesson));

        when(lessonInfoMapper.mapLessonEntityToLessonInfo(any(Lesson.class)))
            .thenReturn(lessonInfo);

        assertEquals(Collections.singletonList(lessonInfo), lessonService.findLessonByCourseName("cName"));
    }

    @Test
    void findStudentLessonsShouldReturnStudentLesson() {
        when(studentRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(new Student(
                UUID.randomUUID(),
                "fn",
                "ln",
                "em",
                "pass",
                group
            )));

        when(lessonRepository.findByGroupGroupName(any(String.class)))
            .thenReturn(Collections.singletonList(lesson));

        when(lessonInfoMapper.mapLessonEntityToLessonInfo(any(Lesson.class)))
            .thenReturn(lessonInfo);

        assertEquals(
            Collections.singletonList(lessonInfo),
            lessonService.findStudentLessons("email")
        );
    }

    @Test
    void changeLessonDateShouldSaveLessonWithNewDate() {
        Lesson newLesson = lesson;

        Date date = Date.valueOf("2022-01-01");

        newLesson.setDate(date);

        when(lessonRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(lesson));

        when(lessonInfoMapper.mapLessonEntityToLessonInfo(lesson))
            .thenReturn(lessonInfo);

        when(lessonRepository.save(any(Lesson.class)))
            .thenReturn(newLesson);

        lessonService.changeLessonDate(lesson.getLessonId(), newLesson.getDate());

        verify(lessonRepository).save(any(Lesson.class));

        assertEquals(date, lessonService.findLessonById(lesson.getLessonId()).getDate());
    }

    @Test
    void changeLessonTimeShouldSaveLessonWithNewTime() {
        lesson.setLessonId(UUID.randomUUID());

        Time newStartTime = Time.valueOf("09:00:00");
        Time newEndTime = Time.valueOf("10:00:00");

        when(lessonRepository.findById(any(UUID.class))).thenReturn(Optional.of(lesson));

        lessonService.changeLessonTime(lesson.getLessonId(), newStartTime, newEndTime);

        verify(lessonRepository).save(lesson);

        assertEquals(newStartTime, lesson.getStartTime());
        assertEquals(newEndTime, lesson.getEndTime());
    }

    @Test
    void changeLessonCourseShouldSaveLessonWithNewCourse() {
        lesson.setLessonId(UUID.randomUUID());

        Course newCourse = new Course();
        newCourse.setCourseId(UUID.randomUUID());

        when(lessonRepository.findById(any(UUID.class))).thenReturn(Optional.of(lesson));
        when(courseRepository.findByCourseName(any(String.class))).thenReturn(Optional.of(newCourse));

        lessonService.changeLessonCourse(lesson.getLessonId(), newCourse);

        verify(lessonRepository).save(lesson);

        assertEquals(newCourse, lesson.getCourse());
    }

    @Test
    void changeLessonGroupShouldSaveLessonWithNewGroup() {
        lesson.setLessonId(UUID.randomUUID());

        String groupName = "NewGroup";

        Group newGroup = new Group();
        newGroup.setGroupId(UUID.randomUUID());

        when(lessonRepository.findById(any(UUID.class))).thenReturn(Optional.of(lesson));
        when(groupRepository.findByGroupName(any(String.class))).thenReturn(Optional.of(newGroup));

        lessonService.changeLessonGroup(lesson.getLessonId(), groupName);

        verify(lessonRepository).save(lesson);

        assertEquals(newGroup, lesson.getGroup());
    }

    @Test
    void changeLessonTeacherShouldSaveLessonWithNewTeacher() {
        lesson.setLessonId(UUID.randomUUID());

        String teacherEmail = "newteacher@example.com";

        Teacher newTeacher = new Teacher();
        newTeacher.setUserId(UUID.randomUUID());

        when(lessonRepository.findById(any(UUID.class))).thenReturn(Optional.of(lesson));
        when(teacherRepository.findByEmail(teacherEmail)).thenReturn(Optional.of(newTeacher));

        lessonService.changeLessonTeacher(lesson.getLessonId(), teacherEmail);

        verify(lessonRepository).save(lesson);

        assertEquals(newTeacher, lesson.getTeacher());
    }

    @Test
    void deleteLessonShouldDeleteLessonById() {
        UUID lessonId = UUID.randomUUID();

        doNothing().when(lessonRepository).deleteById(lessonId);

        lessonService.deleteLesson(lessonId);

        verify(lessonRepository).deleteById(lessonId);
    }
}
