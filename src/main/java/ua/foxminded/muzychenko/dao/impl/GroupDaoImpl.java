package ua.foxminded.muzychenko.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.dao.exception.GroupNotFoundException;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.entity.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GroupDaoImpl extends AbstractCrudDaoImpl<Group> implements GroupDao {

    private static final String CREATE_QUERY = "INSERT INTO groups VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE groups SET group_name=? WHERE group_id=?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM groups";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM groups WHERE group_id=?";
    private static final String FIND_GROUP_WITH_LESS_OR_EQUAL_STUDENTS_QUERY = """
        SELECT g.group_id, g.group_name
        FROM public.groups AS g
        LEFT JOIN public.users AS u ON g.group_id = u.group_id AND u.user_type = 'Student'
        GROUP BY g.group_id, g.group_name
        HAVING COUNT(u.user_id) <= ?
        """;
    private static final String FIND_STUDENT_GROUP_QUERY = """
        SELECT g.*
        FROM groups g
        JOIN users u ON u.group_id = g.group_id
        WHERE u.user_id =?;
        """;
    private static final String FIND_GROUP_BY_NAME_QUERY = "SELECT * FROM groups WHERE group_name =?";

    @Autowired
    protected GroupDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Group> rowMapper) {
        super(jdbcTemplate, rowMapper, CREATE_QUERY, UPDATE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Group> findGroupWithLessOrEqualStudents(Integer countOfStudents) {
        return jdbcTemplate.query(
            FIND_GROUP_WITH_LESS_OR_EQUAL_STUDENTS_QUERY,
            new Object[]{countOfStudents},
            rowMapper
        );
    }

    @Override
    public Optional<Group> findUsersGroup(UUID id) {
        try {
            Group result = jdbcTemplate.queryForObject(FIND_STUDENT_GROUP_QUERY, new Object[]{id}, rowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            throw new GroupNotFoundException();
        }
    }

    @Override
    public Optional<Group> findByName(String groupName) {
        return findByParam(FIND_GROUP_BY_NAME_QUERY, groupName);
    }

    @Override
    protected Object[] getCreateParameters(Group group) {
        return new Object[]{group.getGroupId(), group.getGroupName()};
    }

    @Override
    protected Object[] getUpdateParameters(UUID id, Group updatedGroup) {
        return new Object[]{updatedGroup.getGroupName(), id};
    }
}
