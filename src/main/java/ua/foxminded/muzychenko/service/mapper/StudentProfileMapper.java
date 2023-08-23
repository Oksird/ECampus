package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;

import java.util.List;

@Mapper
public class StudentProfileMapper {
    public StudentProfile mapStudentInfoToProfile(Student student, Group group, List<Course> courses) {
        GroupInfo groupInfo = new GroupInfo(null);
        if (group != null) {
            groupInfo = new GroupInfo(group.getGroupName());
        }
        List<CourseInfo> courseInfoList = courses.stream()
            .map(course -> new CourseInfo(course.getCourseName(), course.getCourseDescription()))
            .toList();

        return new StudentProfile(
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            groupInfo,
            courseInfoList
        );
    }
}
