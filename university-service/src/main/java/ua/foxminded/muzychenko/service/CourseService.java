package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.UUID;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseValidator courseValidator;
    private final CourseInfoMapper courseInfoMapper;
    private final TeacherProfileMapper teacherProfileMapper;

    @Transactional
    public void createCourse(CourseInfo courseInfo, String teacherEmail) {
        courseValidator.validateCourseInfo(courseInfo);

        Course course = new Course(
            UUID.randomUUID(),
            courseInfo.getCourseName(),
            courseInfo.getCourseDescription()
        );

        course.setTeacher(teacherRepository.findByEmail(teacherEmail).orElseThrow(UserNotFoundException::new));

        courseRepository.save(course);
    }

    @Transactional(readOnly = true)
    public CourseInfo findCourseByName(String courseName) {
        Course course = courseRepository
            .findByCourseName(courseName)
            .orElseThrow(CourseNotFoundException::new);
        return courseInfoMapper.mapCourseEntityToCourseInfo(course);
    }

    @Transactional(readOnly = true)
    public CourseInfo findCourseById(UUID courseID) {
        Course course = courseRepository.findById(courseID)
            .orElseThrow(CourseNotFoundException::new);
        return courseInfoMapper.mapCourseEntityToCourseInfo(course);
    }

    @Transactional
    public void deleteCourse(String courseName) {
        courseRepository.deleteById(getCourseIdByName(courseName));
    }

    @Transactional(readOnly = true)
    public Page<CourseInfo> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Course> coursePage = courseRepository.findAll(pageable);
        return coursePage.map(courseInfoMapper::mapCourseEntityToCourseInfo);
    }

    @Transactional(readOnly = true)
    public Page<CourseInfo> findCoursesPagesByNamePart(String courseNamePart , Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Course> coursePage = courseRepository.findByCourseNameContainingIgnoreCase(courseNamePart, pageable);
        return coursePage.map(courseInfoMapper::mapCourseEntityToCourseInfo);
    }

    @Transactional(readOnly = true)
    public List<CourseInfo> findAll() {
        List<Course> courseList = courseRepository.findAll();
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

        courseValidator.validateCourseInfo(
            new CourseInfo(
                newCourse.getCourseId().toString(),
                newCourse.getCourseName(),
                newCourse.getCourseDescription(),
                teacherProfileMapper.mapTeacherEntityToProfile(newCourse.getTeacher())
            )
        );

        courseRepository.save(newCourse);
    }

    @Transactional
    public void changeCourseDescription(String courseName, String newCourseDescription) {
        Course oldCourse = courseRepository
            .findByCourseName(courseName)
            .orElseThrow(CourseNotFoundException::new);
        Course newCourse = new Course(oldCourse.getCourseId(), oldCourse.getCourseName(), newCourseDescription);

        courseValidator.validateCourseInfo(
            new CourseInfo(
                newCourse.getCourseId().toString(),
                newCourse.getCourseName(),
                newCourse.getCourseDescription(),
                teacherProfileMapper.mapTeacherEntityToProfile(newCourse.getTeacher())
            )
        );

        courseRepository.save(newCourse);
    }

    @Transactional
    public void changeCourseTeacher(String courseName, String teacherEmail) {
        Course course = courseRepository.findByCourseName(courseName).orElseThrow(CourseNotFoundException::new);
        Teacher teacher = teacherRepository.findByEmail(teacherEmail).orElseThrow(UserNotFoundException::new);

        course.setTeacher(teacher);

        courseRepository.save(course);
    }

    private UUID getCourseIdByName(String courseName) {
        return courseRepository
            .findByCourseName(courseName)
            .orElseThrow(CourseNotFoundException::new)
            .getCourseId();
    }
}
