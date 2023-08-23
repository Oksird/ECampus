package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dao.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.service.mapper.CourseInfoMapper;
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

@SpringJUnitConfig(TestConfig.class)
class CourseServiceTest {

    @MockBean
    private CourseDao courseDao;
    @MockBean
    private CourseValidator courseValidator;
    @MockBean
    private CourseInfoMapper courseInfoMapper;
    @Autowired
    private CourseService courseService;

    @Test
    void createCourseShouldCreateNewCourseWithCorrectFields() {
        doNothing()
            .when(courseValidator)
            .validateCourseInfo(any(CourseInfo.class));
        doNothing()
            .when(courseDao)
            .create(any(Course.class));

        CourseInfo courseInfo = new CourseInfo("name", "desc");

        courseService.createCourse(courseInfo);

        verify(courseValidator).validateCourseInfo(courseInfo);
        verify(courseDao).create(any(Course.class));
    }

    @Test
    void findCourseByNameShouldReturnCourseInfo() {

        Course course = new Course(UUID.randomUUID(), "cn", "cdesc");

        when(courseDao.findByName(any(String.class)))
            .thenReturn(Optional.of(course));

        CourseInfo expectedCourseInfo = new CourseInfo(course.getCourseName(), course.getCourseDescription());

        when(courseInfoMapper.mapCourseEntityToCourseInfo(course))
            .thenReturn(expectedCourseInfo);

        assertEquals(expectedCourseInfo, courseService.findCourseByName("cn"));
    }

    @Test
    void findCourseByIdShouldReturnCourseWhenIdIsCorrect() {
        Course course = new Course(UUID.randomUUID(), "cn", "cdesc");

        when(courseDao.findById(any(UUID.class)))
            .thenReturn(Optional.of(course));

        CourseInfo courseInfo = new CourseInfo(course.getCourseName(), course.getCourseDescription());

        when(courseInfoMapper.mapCourseEntityToCourseInfo(course))
            .thenReturn(courseInfo);

        assertEquals(courseInfo, courseService.findCourseById(UUID.randomUUID()));
    }

    @Test
    void deleteCourseShouldDeleteCourseFromDataBase() {
        Course course = new Course(UUID.randomUUID(), "cn", "cdesc");

        when(courseDao.findByName(any(String.class)))
            .thenReturn(Optional.of(course));

        doNothing()
            .when(courseDao)
            .deleteById(any(UUID.class));

        courseService.deleteCourse("cn");

        verify(courseDao).deleteById(any(UUID.class));
    }

    @Test
    void findAllCoursesShouldReturnAllCoursesByPage() {
        Course course1 = new Course(UUID.randomUUID(), "cn1", "cdesc1");
        Course course2 = new Course(UUID.randomUUID(), "cn2", "cdesc2");

        List<Course> expectedCourses = new ArrayList<>(List.of(course1, course2));

        CourseInfo courseInfo1 = new CourseInfo(course1.getCourseName(), course1.getCourseDescription());
        CourseInfo courseInfo2 = new CourseInfo(course2.getCourseName(), course2.getCourseDescription());

        List<CourseInfo> courseInfoList = new ArrayList<>(List.of(courseInfo1, courseInfo2));

        when(courseDao.findAll(any(Long.class), any(Long.class)))
            .thenReturn(expectedCourses);

        when(courseInfoMapper.mapCourseEntityToCourseInfo(course1))
            .thenReturn(courseInfo1);
        when(courseInfoMapper.mapCourseEntityToCourseInfo(course2))
            .thenReturn(courseInfo2);

        assertEquals(courseInfoList, courseService.findAllCourses(1L, 1L));
    }

    @Test
    void changeCourseNameShouldChangeCourseNameIfNewNameIsCorrect() {
        Course course = new Course(UUID.randomUUID(), "cn1", "cdesc1");

        when(courseDao.findByName(any(String.class)))
            .thenReturn(Optional.of(course));

        doNothing()
            .when(courseValidator)
            .validateCourseInfo(any(CourseInfo.class));

        doNothing()
            .when(courseDao)
            .update(any(UUID.class), any(Course.class));

        courseService.changeCourseName("cn", "cnn");

        verify(courseDao).findByName("cn");
        verify(courseValidator).validateCourseInfo(any(CourseInfo.class));
        verify(courseDao).update(any(UUID.class), any(Course.class));
    }

    @Test
    void changeCourseDescriptionShouldChangeCourseDescriptionIfNewDescriptionIsCorrect() {
        Course course = new Course(UUID.randomUUID(), "cn1", "cdesc1");

        when(courseDao.findByName(any(String.class)))
            .thenReturn(Optional.of(course));

        doNothing()
            .when(courseValidator)
            .validateCourseInfo(any(CourseInfo.class));

        doNothing()
            .when(courseDao)
            .update(any(UUID.class), any(Course.class));

        courseService.changeCourseDescription("cn", "description example");

        verify(courseDao).findByName("cn");
        verify(courseValidator).validateCourseInfo(any(CourseInfo.class));
        verify(courseDao).update(any(UUID.class), any(Course.class));
    }

    @Test
    void findCourseByNameShouldThrowExceptionWhenNameDoesNotExist() {
        when(courseDao.findByName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.findCourseByName("bad name"));
    }

    @Test
    void findCourseByIdShouldThrowExceptionWhenCourseDoesNotExist() {
        when(courseDao.findById(any(UUID.class)))
            .thenReturn(Optional.empty());
        UUID wrongUUID = UUID.randomUUID();
        assertThrows(CourseNotFoundException.class, () -> courseService.findCourseById(wrongUUID));
    }

    @Test
    void changeCourseNameShouldThrowExceptionWhenCourseWithNameDoesNotExist() {
        when(courseDao.findByName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(
            CourseNotFoundException.class,
            () -> courseService.changeCourseName("wrongName", "newName")
        );
    }

    @Test
    void changeCourseDescriptionShouldThrowExceptionWhenCourseWithNameDoesNotExist() {
        when(courseDao.findByName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(
            CourseNotFoundException.class,
            () -> courseService.changeCourseDescription("wrongName", "desc")
        );
    }

    @Test
    void deleteCourseShouldThrowExceptionWhenCourseWithIdDoesNotExist() {
        when(courseDao.findByName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(
            CourseNotFoundException.class,
            () -> courseService.deleteCourse("wrongName"));
    }
}
