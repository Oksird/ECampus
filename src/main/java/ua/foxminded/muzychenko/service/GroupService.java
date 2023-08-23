package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.dao.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.service.mapper.GroupInfoMapper;
import ua.foxminded.muzychenko.service.validator.GroupValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupDao groupDao;
    private final GroupValidator groupValidator;
    private final GroupInfoMapper groupInfoMapper;

    @Transactional
    public void createGroup(GroupInfo groupInfo) {
        groupValidator.validateGroupName(groupInfo);
        groupDao.create(new Group(UUID.randomUUID(), groupInfo.getGroupName()));
    }

    public GroupInfo findGroupByName(String groupName) {
        Group group = groupDao
            .findByName(groupName)
            .orElseThrow(GroupNotFoundException::new);
        return groupInfoMapper.mapGroupEntityToGroupInfo(group);
    }

    public GroupInfo findGroupById(UUID groupId) {
        Group group = groupDao.findById(groupId)
            .orElseThrow(GroupNotFoundException::new);
        return groupInfoMapper.mapGroupEntityToGroupInfo(group);
    }

    @Transactional
    public void deleteGroup(String groupName) {
        groupDao.deleteById(
            groupDao
            .findByName(groupName)
            .orElseThrow(GroupNotFoundException::new)
            .getGroupId()
        );
    }

    public List<GroupInfo> findAllGroups(Long pageNumber, Long pageSize) {
        List<Group> groups = groupDao.findAll(pageNumber, pageSize);
        List<GroupInfo> groupInfoList = new ArrayList<>(groups.size());
        groups.forEach(group -> groupInfoList.add(groupInfoMapper.mapGroupEntityToGroupInfo(group)));
        return groupInfoList;
    }

    @Transactional
    public void changeGroupName(String groupName, String newGroupName) {
        Group oldGroup = groupDao
            .findByName(groupName)
            .orElseThrow(GroupNotFoundException::new);
        Group newGroup = new Group(oldGroup.getGroupId(), newGroupName);

        groupValidator.validateGroupName(new GroupInfo(newGroup.getGroupName()));

        groupDao.update(oldGroup.getGroupId(), newGroup);
    }

    public List<GroupInfo> findGroupWithLessOrEqualStudents(Integer countOfStudents) {
        List<Group> groups = groupDao.findGroupWithLessOrEqualStudents(countOfStudents);
        List<GroupInfo> groupInfoList = new ArrayList<>(groups.size());
        groups.forEach(group -> groupInfoList.add(groupInfoMapper.mapGroupEntityToGroupInfo(group)));
        return groupInfoList;
    }
}
