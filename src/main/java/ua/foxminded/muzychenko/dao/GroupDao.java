package ua.foxminded.muzychenko.dao;

import ua.foxminded.muzychenko.entity.GroupEntity;

import java.util.List;

public interface GroupDao extends CrudDao<GroupEntity, Long> {

    List<GroupEntity> findGroupWithLessOrEqualStudents(Integer countOfStudents);
}
