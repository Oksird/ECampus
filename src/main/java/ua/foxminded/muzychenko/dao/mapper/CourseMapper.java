package ua.foxminded.muzychenko.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.foxminded.muzychenko.entity.Course;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class CourseMapper implements RowMapper<Course> {
    @Override
    public Course mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Course(
            resultSet.getObject("course_id", UUID.class),
            resultSet.getString("course_name"),
            resultSet.getString("course_description")
        );
    }
}
