package ua.foxminded.muzychenko.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.entity.GroupEntity;

import java.util.List;

@Repository
public class GroupDaoImpl extends AbstractCrudDaoImpl<GroupEntity> implements GroupDao {

    private static final String CREATE_QUERY = "INSERT INTO groups VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE groups SET group_name=? WHERE group_id=?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM groups";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM groups WHERE group_id=?";
    private static final String FIND_GROUP_WITH_LESS_OR_EQUAL_STUDENTS_QUERY = """
        WITH group_counts AS ( SELECT g.group_id, g.group_name, COUNT(s.student_id)
        AS student_count FROM groups g
        LEFT JOIN students s ON g.group_id = s.group_id GROUP BY g.group_id, g.group_name )
        SELECT group_id, group_name, student_count
        FROM group_counts WHERE student_count <= ?;
        """;

    @Autowired
    protected GroupDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<GroupEntity> rowMapper) {
        super(jdbcTemplate, rowMapper, CREATE_QUERY, UPDATE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<GroupEntity> findGroupWithLessOrEqualStudents(Integer countOfStudents) {
        return jdbcTemplate.query(
            FIND_GROUP_WITH_LESS_OR_EQUAL_STUDENTS_QUERY,
            new Object[]{countOfStudents},
            rowMapper
        );
    }

    @Override
    protected Object[] getCreateParameters(GroupEntity group) {
        return new Object[]{group.getGroupId(), group.getGroupName()};
    }

    @Override
    protected Object[] getUpdateParameters(Long id, GroupEntity updatedGroup) {
        return new Object[]{updatedGroup.getGroupName(), id};
    }
}
