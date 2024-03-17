package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.exception.UserNotFoundException;
import ua.foxminded.muzychenko.repository.CourseRepository;
import ua.foxminded.muzychenko.repository.TeacherRepository;
import ua.foxminded.muzychenko.service.mapper.CourseInfoMapper;
import ua.foxminded.muzychenko.service.mapper.TeacherProfileMapper;
import ua.foxminded.muzychenko.service.validator.CourseValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = CourseService.class)
class CourseServiceTest {

    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private CourseValidator courseValidator;
    @MockBean
    private CourseInfoMapper courseInfoMapper;
    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private TeacherProfileMapper teacherProfileMapper;
    @Autowired
    private CourseService courseService;

    @Test
    void createCourseShouldCreateNewCourseWithCorrectFields() {
        Course course = new Course(UUID.randomUUID(), "cn", "cd");

        doNothing()
            .when(courseValidator)
            .validateCourseInfo(any(CourseInfo.class));
        when(courseRepository.save(any(Course.class)))
             .thenReturn(course);

        CourseInfo courseInfo = new CourseInfo(
            course.getCourseId().toString(),
            course.getCourseName(),
            course.getCourseDescription(),
            null
        );

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "num",
            "address"
        );

        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        courseService.createCourse(courseInfo, "gemail@mail.com");

        verify(courseValidator).validateCourseInfo(courseInfo);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void findCourseByNameShouldReturnCourseInfo() {

        Course course = new Course(UUID.randomUUID(), "cn", "cdesc");

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(course));

        CourseInfo expectedCourseInfo = new CourseInfo(
            course.getCourseId().toString(),
            course.getCourseName(),
            course.getCourseDescription(),
            null
        );

        when(courseInfoMapper.mapCourseEntityToCourseInfo(course))
            .thenReturn(expectedCourseInfo);

        assertEquals(expectedCourseInfo, courseService.findCourseByName("cn"));
    }

    @Test
    void findCourseByIdShouldReturnCourseWhenIdIsCorrect() {
        Course course = new Course(UUID.randomUUID(), "cn", "cdesc");

        when(courseRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(course));

        CourseInfo courseInfo = new CourseInfo(
            course.getCourseId().toString(),
            course.getCourseName(),
            course.getCourseDescription(),
            null
        );

        when(courseInfoMapper.mapCourseEntityToCourseInfo(course))
            .thenReturn(courseInfo);

        assertEquals(courseInfo, courseService.findCourseById(UUID.randomUUID()));
    }

    @Test
    void deleteCourseShouldDeleteCourseFromDataBase() {
        Course course = new Course(UUID.randomUUID(), "cn", "cdesc");

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(course));

        doNothing()
            .when(courseRepository)
            .deleteById(any(UUID.class));

        courseService.deleteCourse("cn");

        verify(courseRepository).deleteById(any(UUID.class));
    }

    @Test
    void testGetCoursesPage() {
        List<Course> courses = new ArrayList<>(List.of(
            new Course(UUID.randomUUID(), "cn1", "cdesc1"),
            new Course(UUID.randomUUID(), "cn2", "cdesc2"))
        );

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Course> coursePage = new PageImpl<>(courses, pageRequest, courses.size());

        when(courseRepository.findAll(pageRequest)).thenReturn(coursePage);

        when(courseInfoMapper.mapCourseEntityToCourseInfo(any())).thenAnswer(
            invocation -> {
                Course courseEntity = invocation.getArgument(0);
                return new CourseInfo(
                    courseEntity.getCourseId().toString(),
                    courseEntity.getCourseName(),
                    courseEntity.getCourseDescription(),
                    null
                );
            });

        Page<CourseInfo> result = courseService.findAll(1, 10);

        assertEquals(courses.size(), result.getTotalElements());
    }

    @Test
    void changeCourseNameShouldChangeCourseNameIfNewNameIsCorrect() {
        Course course = new Course(UUID.randomUUID(), "cn1", "cdesc1");

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(course));

        doNothing()
            .when(courseValidator)
            .validateCourseInfo(any(CourseInfo.class));

        when(courseRepository.save(any(Course.class)))
            .thenReturn(course);

        courseService.changeCourseName("cn", "cnn");

        verify(courseRepository).findByCourseName("cn");
        verify(courseValidator).validateCourseInfo(any(CourseInfo.class));
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void changeCourseDescriptionShouldChangeCourseDescriptionIfNewDescriptionIsCorrect() {
        Course course = new Course(UUID.randomUUID(), "cn1", "cdesc1");

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(course));

        doNothing()
            .when(courseValidator)
            .validateCourseInfo(any(CourseInfo.class));

        when(courseRepository.save(any(Course.class)))
            .thenReturn(course);

        courseService.changeCourseDescription("cn", "description example");

        verify(courseRepository).findByCourseName("cn");
        verify(courseValidator).validateCourseInfo(any(CourseInfo.class));
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void findCourseByNameShouldThrowExceptionWhenNameDoesNotExist() {
        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.findCourseByName("bad name"));
    }

    @Test
    void findCourseByIdShouldThrowExceptionWhenCourseDoesNotExist() {
        when(courseRepository.findById(any(UUID.class)))
            .thenReturn(Optional.empty());
        UUID wrongUUID = UUID.randomUUID();
        assertThrows(CourseNotFoundException.class, () -> courseService.findCourseById(wrongUUID));
    }

    @Test
    void changeCourseNameShouldThrowExceptionWhenCourseWithNameDoesNotExist() {
        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(
            CourseNotFoundException.class,
            () -> courseService.changeCourseName("wrongName", "newName")
        );
    }

    @Test
    void changeCourseDescriptionShouldThrowExceptionWhenCourseWithNameDoesNotExist() {
        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(
            CourseNotFoundException.class,
            () -> courseService.changeCourseDescription("wrongName", "desc")
        );
    }

    @Test
    void deleteCourseShouldThrowExceptionWhenCourseWithIdDoesNotExist() {
        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(
            CourseNotFoundException.class,
            () -> courseService.deleteCourse("wrongName"));
    }

    @Test
    void findAllCoursesShouldReturnAllCourses() {
        Course course1 = new Course(UUID.randomUUID(), "cn1", "cd1");
        Course course2 = new Course(UUID.randomUUID(), "cn2", "cd2");

        CourseInfo courseInfo1 = new CourseInfo(
            course1.getCourseId().toString(),
            course1.getCourseName(),
            course1.getCourseDescription(),
            null
        );
        CourseInfo courseInfo2 = new CourseInfo(
            course2.getCourseId().toString(),
            course2.getCourseName(),
            course2.getCourseDescription(),
            null
        );

        when(courseRepository.findAll())
            .thenReturn(new ArrayList<>(List.of(course1, course2)));

        when(courseInfoMapper.mapCourseEntityToCourseInfo(course1))
            .thenReturn(courseInfo1);

        when(courseInfoMapper.mapCourseEntityToCourseInfo(course2))
            .thenReturn(courseInfo2);

        courseService.findAll();

        assertEquals(new ArrayList<>(List.of(courseInfo1, courseInfo2)), courseService.findAll());
    }

    @Test
    void findCoursesPagesByNamePart() {
        Course course1 = new Course(UUID.randomUUID(), "cn1", "cd1");
        Course course2 = new Course(UUID.randomUUID(), "cn2", "cd2");

        CourseInfo courseInfo1 = new CourseInfo(
            course1.getCourseId().toString(),
            course1.getCourseName(),
            course1.getCourseDescription()
            ,null
        );
        CourseInfo courseInfo2 = new CourseInfo(
            course2.getCourseId().toString(),
            course2.getCourseName(),
            course2.getCourseDescription(),
            null
        );

        when(courseRepository.findByCourseNameContainingIgnoreCase(any(String.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(course1, course2)));

        when(courseInfoMapper.mapCourseEntityToCourseInfo(course1))
            .thenReturn(courseInfo1);

        when(courseInfoMapper.mapCourseEntityToCourseInfo(course2))
            .thenReturn(courseInfo2);

        assertEquals(
            new PageImpl<>(List.of(courseInfo1, courseInfo2)),
            courseService.findCoursesPagesByNamePart("asd", 1, 1)
        );
    }

    @Test
    void changeCourseTeacherShouldChangeFieldTeacherInCourseEntity() {
        Course course = new Course(
            UUID.randomUUID(),
            "Cname",
            "Cdesc"
        );

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "num",
            "address"
        );

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(course));
        when(teacherRepository.findByEmail(any(String.class)))
            .thenReturn(Optional.of(teacher));

        when(courseRepository.save(any(Course.class)))
            .thenReturn(course);

        courseService.changeCourseTeacher("cn", "tn");

        assertEquals(courseRepository.findByCourseName("cn").orElseThrow(CourseNotFoundException::new), course);

        assertEquals(teacherRepository.findByEmail("em").orElseThrow(UserNotFoundException::new), teacher);

        assertEquals(
            courseRepository.findByCourseName("cn").orElseThrow(CourseNotFoundException::new).getTeacher(),
            teacher
        );
    }
}
