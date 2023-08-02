package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.Group;

import java.util.List;
import java.util.UUID;

public interface GroupDao extends CrudDao<Group, UUID> {

    List<Group> findGroupWithLessOrEqualStudents(Integer countOfStudents);
}
