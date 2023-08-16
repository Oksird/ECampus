package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.UserType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestConfig.class)
@Transactional
class CourseDaoImplTest {

    @Autowired
    private CourseDao courseDao;

    @DisplayName("Course was created")
    @Test
    void createShouldCreateNewCourse() {
        Course testCourse = new Course(
            UUID.randomUUID(),
            "TestName",
            "TestDesc"
        );
        courseDao.create(testCourse);
        Course actualCourse = courseDao.findById(testCourse.getCourseId()).orElse(null);
        assertEquals(testCourse, actualCourse);
    }

    @DisplayName("Course was deleted")
    @Test
    void deleteByIdShouldDeleteSpecificCourse() {
        int countOfCourses = courseDao.findAll().size();
        courseDao.deleteById(courseDao.findAll().get(0).getCourseId());
        int expectedCountOfCourses = countOfCourses - 1;
        int actualCountOfCourses = courseDao.findAll().size();
        assertEquals(expectedCountOfCourses, actualCountOfCourses);
    }

    @DisplayName("Course was updated")
    @Test
    void updateShouldReplaceCourseWithProvidedOne() {
        Course oldCourse = courseDao.findById(courseDao.findAll().get(0).getCourseId()).orElse(null);
        assert oldCourse != null;
        Course newCourse = new Course(oldCourse.getCourseId(),"TEST", "TEST");
        courseDao.update(oldCourse.getCourseId(), newCourse);
        assertEquals(newCourse, courseDao.findById(oldCourse.getCourseId()).orElse(null));
    }

    @DisplayName("Exception is thrown then user type is admin")
    @Test
    void findCoursesByUserIdAndUserTypeShouldThrowExceptionWhenUserTypeIsAdmin() {
        UUID userId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () ->
            courseDao.findCoursesByUserIdAndUserType(userId, UserType.ADMIN)
        );
    }

    @DisplayName("Runtime exception is thrown when user type is invalid")
    @Test
    void findCoursesByUserIdAndUserTypeWhenUserTypeIsInvalid() {
        UUID userId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () ->
            courseDao.findCoursesByUserIdAndUserType(userId, UserType.valueOf("unknown"))
        );
    }
}
