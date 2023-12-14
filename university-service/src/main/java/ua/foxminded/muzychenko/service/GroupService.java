package ua.foxminded.muzychenko.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dto.profile.CourseInfo;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.exception.CourseNotFoundException;
import ua.foxminded.muzychenko.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.repository.CourseRepository;
import ua.foxminded.muzychenko.repository.GroupRepository;
import ua.foxminded.muzychenko.service.mapper.CourseInfoMapper;
import ua.foxminded.muzychenko.service.mapper.GroupInfoMapper;
import ua.foxminded.muzychenko.service.validator.GroupValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final GroupValidator groupValidator;
    private final GroupInfoMapper groupInfoMapper;
    private final CourseInfoMapper courseInfoMapper;

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

        GroupInfo groupInfo = new GroupInfo(
            newGroup.getGroupId().toString(),
            newGroup.getGroupName(),
            newGroup.getStudents().size(),
            newGroup.getCourses().stream()
                .map(courseInfoMapper::mapCourseEntityToCourseInfo)
                .collect(Collectors.toList())
        );

        groupValidator.validateGroupName(groupInfo);

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
    public List<CourseInfo> findGroupCourses(String groupName) {
        Group group = groupRepository.findByGroupName(groupName).orElseThrow(GroupNotFoundException::new);
        List<CourseInfo> courses = new ArrayList<>();
        group.getCourses().forEach(course -> courses.add(courseInfoMapper.mapCourseEntityToCourseInfo(course)));
        return courses;
    }

    @Transactional(readOnly = true)
    public List<GroupInfo> findGroupsByCourse(CourseInfo courseInfo) {
        Course course = courseRepository.findByCourseName(courseInfo.getCourseName())
            .orElseThrow(CourseNotFoundException::new);
        return course.getGroups().stream().map(groupInfoMapper::mapGroupEntityToGroupInfo)
            .collect(Collectors.toList());
    }

    @Transactional
    public void assignGroupToCourse(String groupName, String courseName) {
        Group group = groupRepository.findByGroupName(groupName).orElseThrow(GroupNotFoundException::new);

        group.getCourses().add(courseRepository.findByCourseName(courseName).orElseThrow(CourseNotFoundException::new));

        groupRepository.save(group);
    }

    @Transactional
    public void excludeGroupFromCourse(String groupName, String courseName) {
        Group group = groupRepository.findByGroupName(groupName).orElseThrow(GroupNotFoundException::new);

        group.getCourses().remove(courseRepository.findByCourseName(courseName).orElseThrow(CourseNotFoundException::new));

        groupRepository.save(group);
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

    @Transactional(readOnly = true)
    public List<GroupInfo> findAll() {
        return groupRepository.findAll()
            .stream()
            .map(groupInfoMapper::mapGroupEntityToGroupInfo)
            .toList();
    }
}
