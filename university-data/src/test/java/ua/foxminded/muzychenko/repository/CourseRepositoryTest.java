package ua.foxminded.muzychenko.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.Course;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = CourseRepository.class)
@Import(DataTestConfig.class)
@Transactional
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findByCourseNameContainingIgnoreCaseShouldReturnPageOfAllCoursesWithNamePart() {
        String courseNamePart = "Cou";

        List<Course> courses = courseRepository.findAll()
            .stream()
            .filter(
                group -> group.getCourseName()
                    .contains(courseNamePart)
            )
            .toList();

        assertEquals(courses ,courseRepository.findByCourseNameContainingIgnoreCase(courseNamePart, PageRequest.of(0, 3)).getContent());
    }
}
