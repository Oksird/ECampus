package ua.foxminded.muzychenko.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.controller.exception.InvalidInputException;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dto.GroupInfo;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.service.validator.GroupValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(TestConfig.class)
class GroupServiceTest {

    @MockBean
    private GroupDao groupDao;
    @MockBean
    private GroupValidator groupValidator;
    @Autowired
    private GroupService groupService;

    @Test
    void createGroupShouldCreateNewGroupWithCorrectFields() {
        doNothing()
            .when(groupValidator)
            .validateGroupName(any(GroupInfo.class));
        doNothing()
            .when(groupDao)
            .create(any(Group.class));

        GroupInfo groupInfo = new GroupInfo("name");

        groupService.createGroup(groupInfo);

        verify(groupValidator).validateGroupName(groupInfo);
        verify(groupDao).create(any(Group.class));
    }

    @Test
    void findGroupByNameShouldReturnGroupInfo() {

        Group group = new Group(UUID.randomUUID(), "gn");

        when(groupDao.findByName(any(String.class)))
            .thenReturn(Optional.of(group));

        GroupInfo expectedGroupInfo = new GroupInfo(group.getGroupName());

        assertEquals(expectedGroupInfo, groupService.findGroupByName("gn"));
    }

    @Test
    void findGroupByIdShouldReturnGroupWhenIdIsCorrect() {
        Group group = new Group(UUID.randomUUID(), "gn");

        when(groupDao.findById(any(UUID.class)))
            .thenReturn(Optional.of(group));

        assertEquals(group, groupService.findGroupById(UUID.randomUUID()));
    }

    @Test
    void deleteGroupShouldDeleteGroupWithEnteredGroupName() {
        Group group = new Group(UUID.randomUUID(), "gn");

        when(groupDao.findByName(any(String.class)))
            .thenReturn(Optional.of(group));

        doNothing()
            .when(groupDao)
            .deleteById(any(UUID.class));

        groupService.deleteGroup("gn");

        verify(groupDao).deleteById(any(UUID.class));
    }

    @Test
    void findAllGroupsShouldReturnAllGroupsByPage() {
        Group group1 = new Group(UUID.randomUUID(), "gn1");
        Group group2 = new Group(UUID.randomUUID(), "gn2");

        List<Group> expectedGroups = new ArrayList<>(List.of(group1, group2));

        when(groupDao.findAll(any(Long.class), any(Long.class)))
            .thenReturn(expectedGroups);

        assertEquals(expectedGroups, groupService.findAllGroups(1L, 1L));
    }

    @Test
    void changeGroupNameShouldChangeGroupNameIfNewNameIsCorrect() {
        Group group = new Group(UUID.randomUUID(), "gn1");

        when(groupDao.findByName(any(String.class)))
            .thenReturn(Optional.of(group));

        doNothing()
            .when(groupValidator)
            .validateGroupName(any(GroupInfo.class));

        doNothing()
            .when(groupDao)
            .update(any(UUID.class), any(Group.class));

        groupService.changeGroupName("gn1", "gn11");

        verify(groupDao).findByName("gn1");
        verify(groupValidator).validateGroupName(any(GroupInfo.class));
        verify(groupDao).update(any(UUID.class), any(Group.class));
    }

    @Test
    void findGroupByIdShouldThrowExceptionWhenGroupDoesNotExist() {
        when(groupDao.findById(any(UUID.class)))
            .thenReturn(Optional.empty());
        UUID wrongUUID = UUID.randomUUID();
        assertThrows(InvalidInputException.class, () -> groupService.findGroupById(wrongUUID));
    }

    @Test
    void changeGroupNameShouldThrowExceptionWhenGroupWithEnteredNameDoesNotExist() {
        when(groupDao.findByName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(
            InvalidInputException.class,
            () -> groupService.changeGroupName("wrongName", "newName")
        );
    }

    @Test
    void deleteCourseShouldThrowExceptionWhenCourseWithIdDoesNotExist() {
        when(groupDao.findByName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(
            InvalidInputException.class,
            () -> groupService.deleteGroup("wrongName"));
    }

    @Test
    void findGroupWithLessOrEqualStudentsShouldReturnGroupsWithLessOrEqualStudentsCount() {
        Group group1 = new Group(UUID.randomUUID(), "gn1");
        Group group2 = new Group(UUID.randomUUID(), "gn2");

        List<Group> groups = new ArrayList<>(List.of(group1, group2));

        when(groupDao.findGroupWithLessOrEqualStudents(any(Integer.class)))
            .thenReturn(groups);
        assertEquals(groups, groupService.findGroupWithLessOrEqualStudents(5));
    }

    @Test
    void findGroupByNameShouldThrowExceptionWhenGroupNameIsIncorrect() {
        when(groupDao.findByName(any(String.class)))
            .thenReturn(Optional.empty());
        assertThrows(InvalidInputException.class, () -> groupService.findGroupByName("gn"));
    }
}