package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.CourseRepository;
import ua.foxminded.muzychenko.dao.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.service.mapper.CourseInfoMapper;
import ua.foxminded.muzychenko.service.validator.CourseValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseValidator courseValidator;
    private final CourseInfoMapper courseInfoMapper;

    @Transactional
    public void createCourse(CourseInfo courseInfo) {
        courseValidator.validateCourseInfo(courseInfo);
        courseRepository.save(new Course(UUID.randomUUID(), courseInfo.getCourseName(), courseInfo.getCourseDescription()));
    }

    public CourseInfo findCourseByName(String courseName) {
        Course course = courseRepository
            .findByCourseName(courseName)
            .orElseThrow(CourseNotFoundException::new);
        return courseInfoMapper.mapCourseEntityToCourseInfo(course);
    }

    public CourseInfo findCourseById(UUID courseID) {
        Course course = courseRepository.findById(courseID)
            .orElseThrow(CourseNotFoundException::new);
        return courseInfoMapper.mapCourseEntityToCourseInfo(course);
    }

    @Transactional
    public void deleteCourse(String courseName) {
        courseRepository.deleteById(getCourseIdByName(courseName));
    }

    public List<CourseInfo> findAllCourses(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Course> courseList = courseRepository.findAll(pageable).getContent();
        List<CourseInfo> courseInfoList = new ArrayList<>(courseList.size());
        courseList.forEach(course -> courseInfoList.add(courseInfoMapper.mapCourseEntityToCourseInfo(course)));
        return courseInfoList;
    }

    @Transactional
    public void changeCourseName(String courseName, String newCourseName) {
        Course oldCourse = courseRepository
            .findByCourseName(courseName)
            .orElseThrow(CourseNotFoundException::new);
        Course newCourse = new Course(oldCourse.getCourseId(), newCourseName, oldCourse.getCourseDescription());

        courseValidator.validateCourseInfo(new CourseInfo(newCourse.getCourseName(), newCourse.getCourseDescription()));

        courseRepository.save(newCourse);
    }

    @Transactional
    public void changeCourseDescription(String courseName, String newCourseDescription) {
        Course oldCourse = courseRepository
            .findByCourseName(courseName)
            .orElseThrow(CourseNotFoundException::new);
        Course newCourse = new Course(oldCourse.getCourseId(), oldCourse.getCourseName(), newCourseDescription);

        courseValidator.validateCourseInfo(new CourseInfo(newCourse.getCourseName(), newCourse.getCourseDescription()));

        courseRepository.save(newCourse);
    }

    private UUID getCourseIdByName(String courseName) {
        return courseRepository
            .findByCourseName(courseName)
            .orElseThrow(CourseNotFoundException::new)
            .getCourseId();
    }
}
