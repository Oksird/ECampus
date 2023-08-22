package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.List;

@Mapper
public class TeacherProfileMapper {
    public TeacherProfile mapTeacherEntityToProfile(Teacher teacher, List<Course> courses) {
        List<CourseInfo> courseInfoList = courses.stream()
            .map(course -> new CourseInfo(course.getCourseName(), course.getCourseDescription()))
            .toList();

        return new TeacherProfile(
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getEmail(),
            courseInfoList
        );
    }
}
