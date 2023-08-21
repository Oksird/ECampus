package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.controller.exception.InvalidInputException;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dto.GroupInfo;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.service.validator.GroupValidator;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupService {
    private static final String INVALID_GROUP_NAME_MESSAGE = "Invalid group name";
    private GroupDao groupDao;
    private GroupValidator groupValidator;

    @Transactional
    public void createGroup(GroupInfo groupInfo) {
        groupValidator.validateGroupName(groupInfo);
        groupDao.create(new Group(UUID.randomUUID(), groupInfo.getGroupName()));
    }

    public GroupInfo findGroupByName(String groupName) {
        Group group = groupDao
            .findByName(groupName)
            .orElseThrow(
                () -> new InvalidInputException(INVALID_GROUP_NAME_MESSAGE)
            );
        return new GroupInfo(group.getGroupName());
    }

    public Group findGroupById(UUID groupId) {
        return groupDao.findById(groupId)
            .orElseThrow(
                () -> new InvalidInputException("Invalid group id")
            );
    }

    @Transactional
    public void deleteGroup(String groupName) {
        groupDao.deleteById(
            groupDao
            .findByName(groupName)
            .orElseThrow(() -> new InvalidInputException(INVALID_GROUP_NAME_MESSAGE))
            .getGroupId()
        );
    }

    public List<Group> findAllGroups(Long pageNumber, Long pageSize) {
        return groupDao.findAll(pageNumber, pageSize);
    }

    @Transactional
    public void changeGroupName(String groupName, String newGroupName) {
        Group oldGroup = groupDao
            .findByName(groupName)
            .orElseThrow(() -> new InvalidInputException(INVALID_GROUP_NAME_MESSAGE)
            );
        Group newGroup = new Group(oldGroup.getGroupId(), newGroupName);

        groupValidator.validateGroupName(new GroupInfo(newGroup.getGroupName()));

        groupDao.update(oldGroup.getGroupId(), newGroup);
    }

    public List<Group> findGroupWithLessOrEqualStudents(Integer countOfStudents) {
        return groupDao.findGroupWithLessOrEqualStudents(countOfStudents);
    }
}
