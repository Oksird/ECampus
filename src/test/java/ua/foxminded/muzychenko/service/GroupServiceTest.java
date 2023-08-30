package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.GroupRepository;
import ua.foxminded.muzychenko.dao.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.service.mapper.GroupInfoMapper;
import ua.foxminded.muzychenko.service.validator.GroupValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(TestConfig.class)
class GroupServiceTest {

    @MockBean
    private GroupRepository groupRepository;
    @MockBean
    private GroupValidator groupValidator;
    @MockBean
    private GroupInfoMapper groupInfoMapper;
    @Autowired
    private GroupService groupService;

    @Test
    void createGroupShouldCreateNewGroupWithCorrectFields() {
        doNothing()
            .when(groupValidator)
            .validateGroupName(any(GroupInfo.class));
        when(groupRepository.save(any(Group.class)))
             .thenReturn(new Group(UUID.randomUUID(), "gn"));

        GroupInfo groupInfo = new GroupInfo("name");

        groupService.createGroup(groupInfo);

        verify(groupValidator).validateGroupName(groupInfo);
        verify(groupRepository).save(any(Group.class));
    }

    @Test
    void findGroupByNameShouldReturnGroupInfo() {

        Group group = new Group(UUID.randomUUID(), "gn");

        when(groupRepository.findByGroupName(any(String.class)))
            .thenReturn(Optional.of(group));

        GroupInfo expectedGroupInfo = new GroupInfo(group.getGroupName());

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group))
            .thenReturn(expectedGroupInfo);

        assertEquals(expectedGroupInfo, groupService.findGroupByName("gn"));
    }

    @Test
    void findGroupByIdShouldReturnGroupWhenIdIsCorrect() {
        Group group = new Group(UUID.randomUUID(), "gn");

        when(groupRepository.findById(any(UUID.class)))
            .thenReturn(Optional.of(group));

        GroupInfo groupInfo = new GroupInfo(group.getGroupName());

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

        GroupInfo groupInfo1 = new GroupInfo(group1.getGroupName());
        GroupInfo groupInfo2 = new GroupInfo(group2.getGroupName());

        List<Group> expectedGroups = new ArrayList<>(List.of(group1, group2));
        List<GroupInfo> groupInfoList = new ArrayList<>(List.of(groupInfo1, groupInfo2));

        Page<Group> expectedPage = new PageImpl<>(expectedGroups);

        when(groupRepository.findAll(any(Pageable.class)))
                .thenReturn(expectedPage);

        when(groupInfoMapper.mapGroupEntityToGroupInfo(group1))
            .thenReturn(groupInfo1);
        when(groupInfoMapper.mapGroupEntityToGroupInfo(group2))
            .thenReturn(groupInfo2);

        assertEquals(groupInfoList, groupService.findAllGroups(1, 1));
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

        GroupInfo groupInfo1 = new GroupInfo(group1.getGroupName());
        GroupInfo groupInfo2 = new GroupInfo(group2.getGroupName());

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
}