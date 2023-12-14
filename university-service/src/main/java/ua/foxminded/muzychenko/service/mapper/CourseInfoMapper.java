package ua.foxminded.muzychenko.service.mapper;

import lombok.AllArgsConstructor;
import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;

@Mapper
@AllArgsConstructor
public class CourseInfoMapper {

    public CourseInfo mapCourseEntityToCourseInfo(Course course) {

        Teacher teacher = course.getTeacher();

        return new CourseInfo(
            course.getCourseId().toString(),
            course.getCourseName(), course.getCourseDescription(),
            new TeacherProfile(
                teacher.getUserId().toString(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail(),
                teacher.getPhoneNumber(),
                teacher.getAddress()
            )
        );
    }

}
