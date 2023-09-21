package ua.foxminded.muzychenko.university.service.mapper;

import ua.foxminded.muzychenko.university.config.Mapper;
import ua.foxminded.muzychenko.university.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.university.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.university.entity.Course;
import ua.foxminded.muzychenko.university.entity.Teacher;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public class TeacherProfileMapper {
    public TeacherProfile mapTeacherEntityToProfile(Teacher teacher, Set<Course> courses) {
        Set<CourseInfo> courseInfos = courses.stream()
            .map(course -> new CourseInfo(course.getCourseId().toString(), course.getCourseName(), course.getCourseDescription()))
            .collect(Collectors.toSet());

        return new TeacherProfile(
            teacher.getUserId().toString(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            courseInfos
        );
    }
}
