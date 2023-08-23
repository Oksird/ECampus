package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupDao extends CrudDao<Group, UUID> {

    List<Group> findGroupWithLessOrEqualStudents(Integer countOfStudents);

    Optional<Group> findUsersGroup(UUID userId);

    Optional<Group> findByName(String groupName);
}
