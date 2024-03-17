package ua.foxminded.muzychenko.service.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GroupInfoMapper.class, CourseInfoMapper.class})
class GroupInfoMapperTest {

    @Autowired
    private GroupInfoMapper mapper;
    @MockBean
    private CourseInfoMapper courseInfoMapper;

    @Test
    void mapGroupEntityToGroupInfoShouldReturnGroupInfoBasedOnGroupEntity() {
        Group group = new Group(
            UUID.randomUUID(),
            "gn"
        );

        Course course1 = new Course();
        Course course2 = new Course();

        group.setCourses(List.of(course1, course2));

        CourseInfo courseInfo = new CourseInfo(
            UUID.randomUUID().toString(),
            "cn",
            "cd",
            null
        );

        when(courseInfoMapper.mapCourseEntityToCourseInfo(any(Course.class)))
            .thenReturn(courseInfo);

        GroupInfo expectedGroupInfo = new GroupInfo(
            group.getGroupId().toString(),
            group.getGroupName(),
            group.getStudents().size(),
            group.getCourses().stream().map(course -> courseInfoMapper.mapCourseEntityToCourseInfo(course)).toList()
        );

        assertEquals(expectedGroupInfo, mapper.mapGroupEntityToGroupInfo(group));
    }
}
