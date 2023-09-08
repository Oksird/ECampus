package ua.foxminded.muzychenko.university.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.university.TestUniversityApplication;
import ua.foxminded.muzychenko.university.entity.Course;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringJUnitConfig(TestUniversityApplication.class)
@Transactional
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @DisplayName("Course was created")
    @Test
    void createShouldCreateNewCourse() {
        Course testCourse = new Course(
            UUID.randomUUID(),
            "TestName",
            "TestDesc"
        );
        courseRepository.save(testCourse);
        Course actualCourse = courseRepository.findById(testCourse.getCourseId()).orElse(null);
        assertEquals(testCourse, actualCourse);
    }

    @DisplayName("Course was deleted")
    @Test
    void deleteByIdShouldDeleteSpecificCourse() {
        int countOfCourses = courseRepository.findAll().size();
        courseRepository.deleteById(courseRepository.findAll().get(0).getCourseId());
        int expectedCountOfCourses = countOfCourses - 1;
        int actualCountOfCourses = courseRepository.findAll().size();
        assertEquals(expectedCountOfCourses, actualCountOfCourses);
    }

    @DisplayName("Course was updated")
    @Test
    void updateShouldReplaceCourseWithProvidedOne() {
        Course oldCourse = courseRepository.findById(courseRepository.findAll().get(0).getCourseId()).orElse(null);
        assert oldCourse != null;
        Course newCourse = new Course(oldCourse.getCourseId(),"TEST", "TEST");
        courseRepository.save(newCourse);
        assertEquals(newCourse, courseRepository.findById(oldCourse.getCourseId()).orElse(null));
    }
}
