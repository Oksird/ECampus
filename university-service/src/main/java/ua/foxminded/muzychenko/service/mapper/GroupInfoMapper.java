package ua.foxminded.muzychenko.service.mapper;

import lombok.AllArgsConstructor;
import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.entity.Group;

import java.util.List;

@Mapper
@AllArgsConstructor
public class GroupInfoMapper {

    private CourseInfoMapper courseInfoMapper;

    public GroupInfo mapGroupEntityToGroupInfo(Group group) {

        List<CourseInfo> courses = group.getCourses().stream()
            .map(course -> courseInfoMapper.mapCourseEntityToCourseInfo(course))
            .toList();

        return new GroupInfo(
            group.getGroupId().toString(),
            group.getGroupName(),
            group.getStudents().size(),
            courses
        );
    }
}
