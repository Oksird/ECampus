package ua.foxminded.muzychenko.service.mapper;

import ua.foxminded.muzychenko.config.Mapper;
import ua.foxminded.muzychenko.dto.profile.GroupInfo;
import ua.foxminded.muzychenko.entity.Group;

@Mapper
public class GroupInfoMapper {
    public GroupInfo mapGroupEntityToGroupInfo(Group group) {
        return new GroupInfo(group.getGroupName());
    }
}
