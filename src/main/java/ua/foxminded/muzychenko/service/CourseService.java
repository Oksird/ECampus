package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.controller.exception.InvalidInputException;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.dto.CourseInfo;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.service.validator.CourseValidator;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CourseService {
    private CourseDao courseDao;
    private CourseValidator courseValidator;
    private static final String INVALID_COURSE_NAME_MESSAGE = "Invalid course name";

    @Transactional
    public void createCourse(CourseInfo courseInfo) {
        courseValidator.validateCourseInfo(courseInfo);
        courseDao.create(new Course(UUID.randomUUID(), courseInfo.getCourseName(), courseInfo.getCourseDescription()));
    }

    public CourseInfo findCourseByName(String courseName) {
        Course course = courseDao
            .findByName(courseName)
            .orElseThrow(
                () -> new InvalidInputException(INVALID_COURSE_NAME_MESSAGE)
            );
        return new CourseInfo(course.getCourseName(), course.getCourseDescription());
    }

    public Course findCourseById(UUID courseID) {
        return courseDao.findById(courseID)
            .orElseThrow(
                () -> new InvalidInputException("Invalid course id")
            );
    }

    @Transactional
    public void deleteCourse(String courseName) {
        courseDao.deleteById(getCourseIdByName(courseName));
    }

    public List<Course> findAllCourses(Long pageNumber, Long pageSize) {
        return courseDao.findAll(pageNumber, pageSize);
    }

    @Transactional
    public void changeCourseName(String courseName, String newCourseName) {
        Course oldCourse = courseDao
            .findByName(courseName)
            .orElseThrow(() -> new InvalidInputException(INVALID_COURSE_NAME_MESSAGE)
            );
        Course newCourse = new Course(oldCourse.getCourseId(), newCourseName, oldCourse.getCourseDescription());

        courseValidator.validateCourseInfo(new CourseInfo(newCourse.getCourseName(), newCourse.getCourseDescription()));

        courseDao.update(oldCourse.getCourseId(), newCourse);
    }

    @Transactional
    public void changeCourseDescription(String courseName, String newCourseDescription) {
        Course oldCourse = courseDao
            .findByName(courseName)
            .orElseThrow(() -> new InvalidInputException(INVALID_COURSE_NAME_MESSAGE)
            );
        Course newCourse = new Course(oldCourse.getCourseId(), oldCourse.getCourseName(), newCourseDescription);

        courseValidator.validateCourseInfo(new CourseInfo(newCourse.getCourseName(), newCourse.getCourseDescription()));

        courseDao.update(oldCourse.getCourseId(), newCourse);
    }

    private UUID getCourseIdByName(String courseName) {
        return courseDao
            .findByName(courseName)
            .orElseThrow(() -> new InvalidInputException(INVALID_COURSE_NAME_MESSAGE))
            .getCourseId();
    }
}
