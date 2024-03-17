package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.dto.profile.TeacherProfile;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Teacher;
import ua.foxminded.muzychenko.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.repository.CourseRepository;
import ua.foxminded.muzychenko.repository.GroupRepository;
import ua.foxminded.muzychenko.service.mapper.CourseInfoMapper;
import ua.foxminded.muzychenko.service.mapper.GroupInfoMapper;
import ua.foxminded.muzychenko.service.validator.GroupValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = GroupService.class)
class GroupServiceTest {

    @MockBean
    private GroupRepository groupRepository;
    @MockBean
    private GroupValidator groupValidator;
    @MockBean
    private GroupInfoMapper groupInfoMapper;
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private CourseInfoMapper courseInfoMapper;
    @Autowired
    private GroupService groupService;

    @Test
    void createGroupShouldCreateNewGroupWithCorrectFields() {
        doNothing()
            .when(groupValidator)
            .validateGroupName(any(GroupInfo.class));
        when(groupRepository.save(any(Group.class)))
             .thenReturn(new Group(UUID.randomUUID(), "gn"));

        GroupInfo groupInfo = new GroupInfo(
            UUID.randomUUID().toString(),
            "name",
            1,
            null
        );

        groupService.createGroup(groupInfo);

        verify(groupValidator).validateGroupName(groupInfo);
        verify(groupRepository).save(any(Group.class));
    }

    @Test
    void findGroupByNameShouldReturnGroupInfo() {

        Group group = new Group(UUID.randomUUID(), "gn");

        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.of(group));

        GroupInfo expectedGroupInfo = new GroupInfo(group.getGroupId().toString(),
            group.getGroupName(),
            1,
            null
        );

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group))
            .thenReturn(expectedGroupInfo);

        assertEquals(expectedGroupInfo, groupService.findGroupByName("gn"));
    }

    @Test
    void findGroupByIdShouldReturnGroupWhenIdIsCorrect() {
        Group group = new Group(UUID.randomUUID(), "gn");

        when(groupRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(group));

        GroupInfo groupInfo = new GroupInfo(
            group.getGroupId().toString(),
            group.getGroupName(),
            1,
            null
        );

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group))
            .thenReturn(groupInfo);

        assertEquals(groupInfo, groupService.findGroupById(UUID.randomUUID()));
    }

    @Test
    void deleteGroupShouldDeleteGroupWithEnteredGroupName() {
        Group group = new Group(UUID.randomUUID(), "gn");

        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.of(group));

        doNothing()
            .when(groupRepository)
            .deleteById(any(UUID.class));

        groupService.deleteGroup("gn");

        verify(groupRepository).deleteById(any(UUID.class));
    }

    @Test
    void findAllGroupsShouldReturnAllGroupsByPage() {
        Group group1 = new Group(UUID.randomUUID(), "gn1");
        Group group2 = new Group(UUID.randomUUID(), "gn2");

        GroupInfo groupInfo1 = new GroupInfo(
            group1.getGroupId().toString(),
            group1.getGroupName(),
            1,
            null
        );

        GroupInfo groupInfo2 = new GroupInfo(
            group2.getGroupId().toString(),
            group2.getGroupName(),
            1,
            null
        );

        List<Group> expectedGroups = new ArrayList<>(List.of(group1, group2));
        List<GroupInfo> groupInfoList = new ArrayList<>(List.of(groupInfo1, groupInfo2));

        Page<Group> expectedPage = new PageImpl<>(expectedGroups);

        when(groupRepository.findAll(any(Pageable.class)))
                .thenReturn(expectedPage);

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group1))
            .thenReturn(groupInfo1);
        when(groupInfoMapper.mapGroupEntityToGroupInfo(group2))
            .thenReturn(groupInfo2);

        assertEquals(groupInfoList, groupService.findAll(1, 1).getContent());
    }

    @Test
    void changeGroupNameShouldChangeGroupNameIfNewNameIsCorrect() {
        Group group = new Group(UUID.randomUUID(), "gn1");

        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.of(group));

        doNothing()
            .when(groupValidator)
            .validateGroupName(any(GroupInfo.class));

        when(groupRepository.save(any(Group.class)))
                .thenReturn(group);

        groupService.changeGroupName("gn1", "gn11");

        verify(groupRepository).findByGroupName("gn1");
        verify(groupValidator).validateGroupName(any(GroupInfo.class));
        verify(groupRepository).save(any(Group.class));
    }

    @Test
    void findGroupByIdShouldThrowExceptionWhenGroupDoesNotExist() {
        when(groupRepository.findById(any(UUID.class)))
            .thenReturn(Optional.empty());
        UUID wrongUUID = UUID.randomUUID();
        assertThrows(GroupNotFoundException.class, () -> groupService.findGroupById(wrongUUID));
    }

    @Test
    void changeGroupNameShouldThrowExceptionWhenGroupWithEnteredNameDoesNotExist() {
        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(
            GroupNotFoundException.class,
            () -> groupService.changeGroupName("wrongName", "newName")
        );
    }

    @Test
    void deleteCourseShouldThrowExceptionWhenCourseWithIdDoesNotExist() {
        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(
            GroupNotFoundException.class,
            () -> groupService.deleteGroup("wrongName"));
    }

    @Test
    void findGroupWithLessOrEqualStudentsShouldReturnGroupsWithLessOrEqualStudentsCount() {
        Group group1 = new Group(UUID.randomUUID(), "gn1");
        Group group2 = new Group(UUID.randomUUID(), "gn2");

        GroupInfo groupInfo1 = new GroupInfo(
            group1.getGroupId().toString(),
            group1.getGroupName(),
            1,
            null
        );

        GroupInfo groupInfo2 = new GroupInfo(
            group2.getGroupId().toString(),
            group2.getGroupName(),
            1,
            null
        );

        Set<Group> expectedGroups = new HashSet<>(List.of(group1, group2));
        List<GroupInfo> groupInfoList = new ArrayList<>(List.of(groupInfo1, groupInfo2));

        when(groupRepository.findGroupWithLessOrEqualStudents(any(Integer.class)))
            .thenReturn(expectedGroups);

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group1))
            .thenReturn(groupInfo1);
        when(groupInfoMapper.mapGroupEntityToGroupInfo(group2))
            .thenReturn(groupInfo2);
        assertTrue(groupInfoList.containsAll(groupService.findGroupWithLessOrEqualStudents(5)));
    }

    @Test
    void findGroupByNameShouldThrowExceptionWhenGroupNameIsIncorrect() {
        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(GroupNotFoundException.class, () -> groupService.findGroupByName("gn"));
    }

    @Test
    void findGroupsPagesByNamePartShouldReturnPageWithGroupInfos() {
        Group group1 = new Group(UUID.randomUUID(), "gn1");
        Group group2 = new Group(UUID.randomUUID(), "gn2");

        GroupInfo groupInfo1 = new GroupInfo(
            group1.getGroupId().toString(),
            group1.getGroupName(),
            1,
            null
        );

        GroupInfo groupInfo2 = new GroupInfo(
            group2.getGroupId().toString(),
            group2.getGroupName(),
            1,
            null
        );

        List<GroupInfo> groupInfoList = new ArrayList<>(List.of(groupInfo1, groupInfo2));

        when(groupRepository.findByGroupNameContainingIgnoreCase(any(String.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(group1, group2)));

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group1))
            .thenReturn(groupInfo1);

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group2))
            .thenReturn(groupInfo2);

        assertEquals(new PageImpl<>(groupInfoList), groupService.findGroupsPagesByNamePart("aa", 1 , 1));
    }

    @Test
    void findGroupCoursesShouldReturnCoursesThatRelatedToTheGroup() {
        Group group = new Group(
            UUID.randomUUID(),
            "GroupName"
        );

        Course course = new Course(
            UUID.randomUUID(),
            "CourseName",
            "CourseDesc"
        );

        Teacher teacher = new Teacher(
            UUID.randomUUID(),
            "fn",
            "ln",
            "em",
            "pass",
            "num",
            "address"
        );

        course.setTeacher(teacher);

        group.setCourses(Collections.singletonList(course));

        CourseInfo courseInfo = new CourseInfo(
            course.getCourseId().toString(),
            course.getCourseName(),
            course.getCourseDescription(),
            new TeacherProfile(
                teacher.getUserId().toString(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail(),
                teacher.getPhoneNumber(),
                teacher.getAddress()
            )
        );

        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.of(group));

        when(courseInfoMapper.mapCourseEntityToCourseInfo(course))
            .thenReturn(courseInfo);

        assertEquals(Collections.singletonList(courseInfo), groupService.findGroupCourses("AA-01"));
    }

    @Test
    void findGroupsByCourseShouldReturnAllGroupsThatAreRelatedToCourse() {
        Group group1 = new Group(
            UUID.randomUUID(),
            "GroupName1"
        );

        Group group2 = new Group(
            UUID.randomUUID(),
            "GroupName2"
        );

        Course course1 = new Course(
            UUID.randomUUID(),
            "CourseName1",
            "CourseDesc1"
        );

        course1.setGroups(List.of(group1, group2));

        CourseInfo courseInfo = new CourseInfo(
            course1.getCourseId().toString(),
            course1.getCourseName(),
            course1.getCourseDescription(),
            null
        );

        GroupInfo groupInfo1 = new GroupInfo(
            group1.getGroupId().toString(),
            group1.getGroupName(),
            0,
            Collections.singletonList(courseInfo)
        );

        GroupInfo groupInfo2 = new GroupInfo(
            group2.getGroupId().toString(),
            group2.getGroupName(),
            0,
            Collections.singletonList(courseInfo)
        );

        when(courseRepository.findByCourseName(any(String.class)))
            .thenReturn(Optional.of(course1));

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group1))
            .thenReturn(groupInfo1);

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group2))
            .thenReturn(groupInfo2);

        assertEquals(List.of(groupInfo1, groupInfo2)
            , groupService.findGroupsByCourse(courseInfo));
    }

    @Test
    void assignGroupToCourse_shouldAddCourseToGroup() {
        String groupName = "Group1";
        String courseName = "Course1";

        Group group = new Group();
        group.setGroupName(groupName);

        Course course = new Course();
        course.setCourseName(courseName);

        when(groupRepository.findByGroupName(groupName)).thenReturn(Optional.of(group));
        when(courseRepository.findByCourseName(courseName)).thenReturn(Optional.of(course));

        groupService.assignGroupToCourse(groupName, courseName);

        assertTrue(group.getCourses().contains(course));

        verify(groupRepository, times(1)).save(group);
    }
    @Test
    void excludeGroupFromCourseShouldRemoveCourseFromGroup() {
        String groupName = "Group1";
        String courseName = "Course1";

        Group group = new Group();
        group.setGroupName(groupName);

        Course course = new Course();
        course.setCourseName(courseName);

        group.setCourses(new ArrayList<>());
        group.getCourses().add(course);

        when(groupRepository.findByGroupName(groupName)).thenReturn(Optional.of(group));
        when(courseRepository.findByCourseName(courseName)).thenReturn(Optional.of(course));

        groupService.excludeGroupFromCourse(groupName, courseName);

        assertFalse(group.getCourses().contains(course));

        verify(groupRepository, times(1)).save(group);
    }

    @Test
    void findAllShouldReturnListOfGroupInfo() {
        Group group1 = new Group();
        group1.setGroupId(UUID.randomUUID());
        group1.setGroupName("Group 1");

        Group group2 = new Group();
        group2.setGroupId(UUID.randomUUID());
        group2.setGroupName("Group 2");

        List<Group> groups = List.of(group1, group2);

        GroupInfo groupInfo1 = new GroupInfo(
            group1.getGroupId().toString(),
            group1.getGroupName(),
            0,
            null
        );

        GroupInfo groupInfo2 = new GroupInfo(
            group2.getGroupId().toString(),
            group2.getGroupName(),
            0,
            null
        );

        List<GroupInfo> expectedGroupInfos = List.of(groupInfo1, groupInfo2);

        when(groupRepository.findAll()).thenReturn(groups);

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group1)).thenReturn(groupInfo1);
        when(groupInfoMapper.mapGroupEntityToGroupInfo(group2)).thenReturn(groupInfo2);

        List<GroupInfo> actualGroupInfos = groupService.findAll();

        assertEquals(expectedGroupInfos, actualGroupInfos);

        verify(groupRepository, times(1)).findAll();

        verify(groupInfoMapper, times(2)).mapGroupEntityToGroupInfo(any(Group.class));
    }
}
