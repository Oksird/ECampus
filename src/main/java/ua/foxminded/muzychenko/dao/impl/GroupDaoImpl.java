package ua.foxminded.muzychenko.dao.impl;

import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.entity.GroupEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupDaoImpl extends AbstractCrudDaoImpl<GroupEntity> implements GroupDao {

    private static final String SAVE_QUERY = "INSERT INTO groups (group_name) values(?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM groups";
    private static final String UPDATE_QUERY
        = "UPDATE groups SET group_name =? WHERE group_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM groups WHERE group_id = ?";
    private static final String FIND_GROUP_WITH_LESS_OR_EQUAL_STUDENTS
        = "WITH group_counts AS ( SELECT g.group_id, g.group_name, COUNT(s.student_id)"
        + " AS student_count FROM groups g "
        + "LEFT JOIN students s ON g.group_id = s.group_id GROUP BY g.group_id, g.group_name ) "
        + "SELECT group_id, group_name, student_count"
        + " FROM group_counts WHERE student_count <= ?";
    private static final String FIND_ALL_BY_PAGE_QUERY
        = "SELECT * FROM groups ORDER BY group_id DESC LIMIT ? OFFSET ?";

    public GroupDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, UPDATE_QUERY,
            DELETE_BY_ID_QUERY, FIND_ALL_BY_PAGE_QUERY);
    }

    @Override
    public List<GroupEntity> findGroupWithLessOrEqualStudents(Integer countOfStudents) {
        return findAllByLongParam(Long.valueOf(countOfStudents),
            FIND_GROUP_WITH_LESS_OR_EQUAL_STUDENTS);
    }

    @Override
    protected GroupEntity mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new GroupEntity(
            resultSet.getLong("group_id"),
            resultSet.getString("group_name")
        );
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, GroupEntity entity)
        throws SQLException {
        preparedStatement.setString(1, entity.groupName());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, GroupEntity oldEntity, GroupEntity newEntity) throws SQLException {
        preparedStatement.setString(1, newEntity.groupName());
        preparedStatement.setLong(2, newEntity.groupId());
    }

    @Override
    protected void deleteById(PreparedStatement preparedStatement, Long id) throws SQLException {
        preparedStatement.setLong(1, id);
    }
}
