package ua.foxminded.muzychenko.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.entity.StudentEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentMapper implements RowMapper<StudentEntity> {
    @Override
    public StudentEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new StudentEntity(
            resultSet.getLong("student_id"),
            resultSet.getLong("group_id"),
            resultSet.getString("first_name"),
            resultSet.getString("last_name")
        );
    }
}
