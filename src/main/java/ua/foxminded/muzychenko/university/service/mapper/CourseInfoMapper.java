package ua.foxminded.muzychenko.university.service.mapper;

import ua.foxminded.muzychenko.university.config.Mapper;
import ua.foxminded.muzychenko.university.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.university.entity.Course;

@Mapper
public class CourseInfoMapper {
    public CourseInfo mapCourseEntityToCourseInfo(Course course) {
        return new CourseInfo(course.getCourseName(), course.getCourseDescription());
    }
}
