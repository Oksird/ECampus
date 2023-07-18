package ua.foxminded.muzychenko.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.entity.GroupEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupMapper implements RowMapper<GroupEntity> {
    @Override
    public GroupEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new GroupEntity(
            resultSet.getLong("group_id"),
            resultSet.getString("group_name")
        );
    }
}
