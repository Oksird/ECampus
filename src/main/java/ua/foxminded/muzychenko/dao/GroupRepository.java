package ua.foxminded.muzychenko.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.entity.Group;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {

    @Query("SELECT g FROM Group g WHERE size(g.students) <= :countOfStudents")
    Set<Group> findGroupWithLessOrEqualStudents(Integer countOfStudents);

    @Query("SELECT g FROM Group g JOIN g.students s WHERE s.userId = :userId")
    Optional<Group> findUsersGroup(UUID userId);

    Optional<Group> findByGroupName(String groupName);
}
