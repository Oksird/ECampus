package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.StudentProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public class StudentProfileMapper {
    public StudentProfile mapStudentInfoToProfile(Student student, Group group, Set<Course> courses) {
        GroupInfo groupInfo = null;
        if (group != null) {
            groupInfo = new GroupInfo(group.getGroupId().toString() ,group.getGroupName(), group.getStudents().size());
        }
        Set<CourseInfo> courseInfos = courses.stream()
            .map(course -> new CourseInfo(course.getCourseId().toString(), course.getCourseName(), course.getCourseDescription()))
            .collect(Collectors.toSet());

        return new StudentProfile(
            student.getUserId().toString(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            groupInfo,
            courseInfos
        );
    }
}
