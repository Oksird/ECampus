package ua.foxminded.muzychenko.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.DataConfiguration;
import ua.foxminded.muzychenko.DataTestConfig;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.exception.UserNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ContextConfiguration(classes = DataConfiguration.class)
@SpringBootTest(classes = GroupRepository.class)
@Import(DataTestConfig.class)
@Transactional
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StudentRepository studentRepository;

    @DisplayName("Groups with less or equal count of students are found")
    @Test
    void findGroupWithLessOrEqualStudentsShouldReturnAllGroupsWithLessOrEqualStudents() {
        int expectedOfGroupsWithEqualOrLessThenThreeStudents = 6;
        int actualCountOfGroupsWithEqualOrLessThenThreeStudents =
            groupRepository.findGroupWithLessOrEqualStudents(3).size();
        assertEquals(
            expectedOfGroupsWithEqualOrLessThenThreeStudents,
            actualCountOfGroupsWithEqualOrLessThenThreeStudents
        );
    }
    @DisplayName("No groups with less or equal count of students")
    @Test
    void findGroupWithLessOrEqualStudentsShouldReturnEmptyListIfConditionWasNotCompleted() {
        int expectedOfGroupsWithEqualOrLessThenTwoStudents = 5;
        int actualCountOfGroupsWithEqualOrLessThenThreeStudents =
            groupRepository.findGroupWithLessOrEqualStudents(1).size();
        assertEquals(
            expectedOfGroupsWithEqualOrLessThenTwoStudents,
            actualCountOfGroupsWithEqualOrLessThenThreeStudents
        );
    }

    @DisplayName("User's group is found")
    @Test
    void findUsersGroupShouldReturnGroupRelatedToExactUser() {
        String email = "maks.m@mail.com";
        Group expectedGroup = groupRepository.findByGroupName("AA-01").orElse(null);
        assertEquals(
            expectedGroup,
            groupRepository.findUsersGroup(
                studentRepository.findByEmail(email)
                    .orElseThrow(UserNotFoundException::new)
                    .getUserId())
                .orElseThrow(GroupNotFoundException::new));
    }

    @DisplayName("Group is found by name")
    @Test
    void findByNameShouldReturnGroupEntityWithCorrectName() {
        Group expectedGroup = new Group(UUID.randomUUID(), "AA-01");
        assertEquals(expectedGroup.getGroupName(), Objects.requireNonNull(groupRepository.findByGroupName("AA-01").orElse(null)).getGroupName());
    }

    @Test
    void findByGroupNameContainingIgnoreCaseShouldReturnPageOfAllGroupsWithNamePart() {
        String groupNamePart = "A";

        List<Group> groups = groupRepository.findAll()
            .stream()
            .filter(
                group -> group.getGroupName()
                    .contains(groupNamePart)
            )
            .toList();

        assertEquals(groups ,groupRepository.findByGroupNameContainingIgnoreCase(groupNamePart, PageRequest.of(0, 6)).getContent());
    }
}
