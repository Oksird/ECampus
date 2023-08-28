package ua.foxminded.muzychenko.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.TestConfig;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestConfig.class)
@Transactional
class GroupDaoImplTest {

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private StudentDao studentDao;

    @DisplayName("Groups with less or equal count of students are found")
    @Test
    void findGroupWithLessOrEqualStudentsShouldReturnAllGroupsWithLessOrEqualStudents() {
        int expectedOfGroupsWithEqualOrLessThenThreeStudents = 1;
        int actualCountOfGroupsWithEqualOrLessThenThreeStudents =
            groupDao.findGroupWithLessOrEqualStudents(3).size();
        assertEquals(
            expectedOfGroupsWithEqualOrLessThenThreeStudents,
            actualCountOfGroupsWithEqualOrLessThenThreeStudents
        );
    }
    @DisplayName("No groups with less or equal count of students")
    @Test
    void findGroupWithLessOrEqualStudentsShouldReturnEmptyListIfConditionWasNotCompleted() {
        int expectedOfGroupsWithEqualOrLessThenTwoStudents = 0;
        int actualCountOfGroupsWithEqualOrLessThenThreeStudents =
            groupDao.findGroupWithLessOrEqualStudents(1).size();
        assertEquals(
            expectedOfGroupsWithEqualOrLessThenTwoStudents,
            actualCountOfGroupsWithEqualOrLessThenThreeStudents
        );
    }

    @DisplayName("Group was created")
    @Test
    void insertShouldCreateNewGroup() {
        int countOfGroups = groupDao.findAll().size();
        groupDao.create(
            new Group(
                UUID.randomUUID(),
                "asd"
            )
        );
        assertNotEquals(countOfGroups, groupDao.findAll().size());
    }

    @DisplayName("Group is updated")
    @Test
    void updateShouldReplaceGroupWithProvided() {
        Group oldGroup = groupDao.findByName("AA-01").orElse(null);
        assert oldGroup != null;
        Group newGroup = new Group(oldGroup.getGroupId(), "AA-11");

        groupDao.update(oldGroup.getGroupId(), newGroup);

        assertEquals(newGroup.getGroupName(), Objects.requireNonNull(groupDao.findById(oldGroup.getGroupId()).orElse(null)).getGroupName());
    }

    @DisplayName("Group was deleted")
    @Test
    void deleteByIdShouldDeleteGroupById() {
        int countOfGroups = groupDao.findAll().size();
        int expectedCountOfGroups = countOfGroups - 1;
        groupDao.deleteById(groupDao.findAll().get(0).getGroupId());
        assertEquals(expectedCountOfGroups, groupDao.findAll().size());
    }

    @DisplayName("No group with such id")
    @Test
    void findByIdShouldThrowExceptionWhenGroupWithIdDoesntExist() {
        assertThrows(NullPointerException.class, () -> groupDao.findById(UUID.randomUUID()));
    }

    @DisplayName("User's group is found")
    @Test
    void findUsersGroupShouldReturnGroupRelatedToExactUser() {
        Group expectedGroup = groupDao.findByName("AA-01").orElse(null);
        assertEquals(expectedGroup, groupDao.findUsersGroup(Objects.requireNonNull(studentDao.findByEmail("es1").orElse(null)).getUserId()).orElse(null));
    }

    @DisplayName("Group is found by name")
    @Test
    void findByNameShouldReturnGroupEntityWithCorrectName() {
        Group expectedGroup = new Group(UUID.randomUUID(), "AA-01");
        assertEquals(expectedGroup.getGroupName(), Objects.requireNonNull(groupDao.findByName("AA-01").orElse(null)).getGroupName());
    }

    @DisplayName("Exception is thrown when group name is incorrect")
    @Test
    void findByNameShouldThrowException() {
        assertThrows(EmptyResultDataAccessException.class,() -> groupDao.findByName("qweqwe"));
    }

    @DisplayName("Exception is thrown when user does not have group")
    @Test
    void findUsersGroupShouldThrowExceptionWhenUserDoesNotHaveGroup() {
        Student example = new Student(
            UUID.randomUUID(),
            "test",
            "test",
            "test",
            "test",
            null
        );

        studentDao.create(example);
        UUID studentId = example.getUserId();
        assertThrows(EmptyResultDataAccessException.class, () -> groupDao.findUsersGroup(studentId));
    }
}
