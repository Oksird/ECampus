package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public class TeacherProfileMapper {
    public TeacherProfile mapTeacherEntityToProfile(Teacher teacher, Set<Course> courses) {
        Set<CourseInfo> courseInfos = courses.stream()
            .map(course -> new CourseInfo(course.getCourseName(), course.getCourseDescription()))
            .collect(Collectors.toSet());

        return new TeacherProfile(
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            courseInfos
        );
    }
}
