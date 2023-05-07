package ua.foxminded.muzychenko.dao;

import java.util.List;
import ua.foxminded.muzychenko.entity.GroupEntity;

public interface GroupsGenerator extends DataGenerator<GroupEntity> {

    void insertGroups(List<GroupEntity> groups);
}
