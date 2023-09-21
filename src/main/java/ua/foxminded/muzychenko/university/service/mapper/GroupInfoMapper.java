package ua.foxminded.muzychenko.university.service.mapper;

import ua.foxminded.muzychenko.university.config.Mapper;
import ua.foxminded.muzychenko.university.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.university.entity.Group;

@Mapper
public class GroupInfoMapper {
    public GroupInfo mapGroupEntityToGroupInfo(Group group) {
        return new GroupInfo(group.getGroupId().toString(), group.getGroupName());
    }
}
