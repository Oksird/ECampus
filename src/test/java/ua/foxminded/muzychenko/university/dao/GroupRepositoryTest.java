package ua.foxminded.muzychenko.university.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.university.TestUniversityApplication;
import ua.foxminded.muzychenko.university.entity.Group;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringJUnitConfig(TestUniversityApplication.class)
@Transactional
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private StudentRepository studentRepository;

    @DisplayName("Groups with less or equal count of students are found")
    @Test
    void findGroupWithLessOrEqualStudentsShouldReturnAllGroupsWithLessOrEqualStudents() {
        int expectedOfGroupsWithEqualOrLessThenThreeStudents = 1;
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
        int expectedOfGroupsWithEqualOrLessThenTwoStudents = 0;
        int actualCountOfGroupsWithEqualOrLessThenThreeStudents =
            groupRepository.findGroupWithLessOrEqualStudents(1).size();
        assertEquals(
            expectedOfGroupsWithEqualOrLessThenTwoStudents,
            actualCountOfGroupsWithEqualOrLessThenThreeStudents
        );
    }

    @DisplayName("Group was created")
    @Test
    void insertShouldCreateNewGroup() {
        int countOfGroups = groupRepository.findAll().size();
        groupRepository.save(
            new Group(
                UUID.randomUUID(),
                "asd"
            )
        );
        assertNotEquals(countOfGroups, groupRepository.findAll().size());
    }

    @DisplayName("Group is updated")
    @Test
    void updateShouldReplaceGroupWithProvided() {
        Group oldGroup = groupRepository.findByGroupName("AA-01").orElse(null);
        Group newGroup = new Group(Objects.requireNonNull(oldGroup).getGroupId(), "AA-11");

        groupRepository.save(newGroup);

        assertEquals(newGroup.getGroupName(), Objects.requireNonNull(groupRepository.findById(oldGroup.getGroupId()).orElse(null)).getGroupName());
    }

    @DisplayName("Group was deleted")
    @Test
    void deleteByIdShouldDeleteGroupById() {
        int countOfGroups = groupRepository.findAll().size();
        int expectedCountOfGroups = countOfGroups - 1;
        groupRepository.deleteById(groupRepository.findAll().get(0).getGroupId());
        assertEquals(expectedCountOfGroups, groupRepository.findAll().size());
    }

    @DisplayName("User's group is found")
    @Test
    void findUsersGroupShouldReturnGroupRelatedToExactUser() {
        Group expectedGroup = groupRepository.findByGroupName("AA-01").orElse(null);
        assertEquals(expectedGroup, groupRepository.findUsersGroup(Objects.requireNonNull(studentRepository.findByEmail("es1").orElse(null)).getUserId()).orElse(null));
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

        assertEquals(groups ,groupRepository.findByGroupNameContainingIgnoreCase(groupNamePart, PageRequest.of(0, 3)).getContent());
    }
}
