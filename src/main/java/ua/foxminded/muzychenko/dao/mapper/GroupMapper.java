package ua.foxminded.muzychenko.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.entity.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class GroupMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Group(
            resultSet.getObject("group_id", UUID.class),
            resultSet.getString("group_name")
        );
    }
}
