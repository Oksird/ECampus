package ua.foxminded.muzychenko.dao.mapper;

import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.entity.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class AdminMapper implements RowMapper<Admin> {
    @Override
    public Admin mapRow(@NonNull ResultSet resultSet, int rowNum) throws SQLException {
        return new Admin(
            resultSet.getObject("user_id", UUID.class),
            resultSet.getString("first_name"),
            resultSet.getString("last_name"),
            resultSet.getString("email"),
            resultSet.getString("password")
        );
    }
}
