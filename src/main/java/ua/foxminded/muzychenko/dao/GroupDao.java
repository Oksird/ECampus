package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.GroupEntity;

import java.util.Optional;

public interface GroupDao extends CrudDao<GroupEntity, Long> {
    Optional<GroupEntity> findGroupWithLessOrEqualStudents(Integer countOfStudents);
}
