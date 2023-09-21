package ua.foxminded.muzychenko.university.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.university.dao.GroupRepository;
import ua.foxminded.muzychenko.university.dao.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.university.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.university.entity.Group;
import ua.foxminded.muzychenko.university.service.mapper.GroupInfoMapper;
import ua.foxminded.muzychenko.university.service.validator.GroupValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupValidator groupValidator;
    private final GroupInfoMapper groupInfoMapper;

    @Transactional
    public void createGroup(GroupInfo groupInfo) {
        groupValidator.validateGroupName(groupInfo);
        groupRepository.save(new Group(UUID.randomUUID(), groupInfo.getGroupName()));
    }

    @Transactional(readOnly = true)
    public GroupInfo findGroupByName(String groupName) {
        Group group = groupRepository
            .findByGroupName(groupName)
            .orElseThrow(GroupNotFoundException::new);
        return groupInfoMapper.mapGroupEntityToGroupInfo(group);
    }

    @Transactional(readOnly = true)
    public GroupInfo findGroupById(UUID groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(GroupNotFoundException::new);
        return groupInfoMapper.mapGroupEntityToGroupInfo(group);
    }

    @Transactional
    public void deleteGroup(String groupName) {
        groupRepository.deleteById(
            groupRepository
            .findByGroupName(groupName)
            .orElseThrow(GroupNotFoundException::new)
            .getGroupId()
        );
    }

    @Transactional
    public void changeGroupName(String groupName, String newGroupName) {
        Group oldGroup = groupRepository
            .findByGroupName(groupName)
            .orElseThrow(GroupNotFoundException::new);
        Group newGroup = new Group(oldGroup.getGroupId(), newGroupName);

        groupValidator.validateGroupName(new GroupInfo(newGroup.getGroupId().toString(), newGroup.getGroupName()));

        groupRepository.save(newGroup);
    }

    @Transactional(readOnly = true)
    public List<GroupInfo> findGroupWithLessOrEqualStudents(Integer countOfStudents) {
        Set<Group> groups = groupRepository.findGroupWithLessOrEqualStudents(countOfStudents);
        List<GroupInfo> groupInfoList = new ArrayList<>(groups.size());
        groups.forEach(group -> groupInfoList.add(groupInfoMapper.mapGroupEntityToGroupInfo(group)));
        return groupInfoList;
    }

    @Transactional(readOnly = true)
    public Page<GroupInfo> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Group> groupPage = groupRepository.findAll(pageable);
        return groupPage.map(groupInfoMapper::mapGroupEntityToGroupInfo);
    }

    @Transactional(readOnly = true)
    public Page<GroupInfo> findGroupsPagesByNamePart(String courseNamePart , Integer pageNumber, Integer pageSize) {
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Group> groupPage = groupRepository.findByGroupNameContainingIgnoreCase(courseNamePart, pageable);
        return groupPage.map(groupInfoMapper::mapGroupEntityToGroupInfo);
    }
}
