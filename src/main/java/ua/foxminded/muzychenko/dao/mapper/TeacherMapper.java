package ua.foxminded.muzychenko.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.entity.Teacher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class TeacherMapper implements RowMapper<Teacher> {
    @Override
    public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Teacher(
            resultSet.getObject("user_id", UUID.class),
            resultSet.getString("first_name"),
            resultSet.getString("last_name"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getObject("course_id", UUID.class)
        );
    }
}
