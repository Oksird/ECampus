package ua.foxminded.muzychenko.dao;

import java.util.Optional;
import ua.foxminded.muzychenko.entity.GroupEntity;

public interface GroupDao extends CrudDao<GroupEntity, Long> {

    Optional<GroupEntity> findGroupWithLessOrEqualStudents(Integer countOfStudents);
}
