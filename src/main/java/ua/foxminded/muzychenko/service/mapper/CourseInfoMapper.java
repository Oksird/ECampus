package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.entity.Course;

@Mapper
public class CourseInfoMapper {
    public CourseInfo mapCourseEntityToCourseInfo(Course course) {
        return new CourseInfo(course.getCourseName(), course.getCourseDescription());
    }
}
