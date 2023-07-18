package ua.foxminded.muzychenko.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.entity.CourseEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CourseMapper implements RowMapper<CourseEntity> {
    @Override
    public CourseEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new CourseEntity(
            resultSet.getLong("course_id"),
            resultSet.getString("course_name"),
            resultSet.getString("course_description")
        );
    }
}
