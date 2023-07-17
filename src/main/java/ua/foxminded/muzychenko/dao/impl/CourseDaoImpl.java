package ua.foxminded.muzychenko.dao.impl;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.entity.CourseEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class CourseDaoImpl extends AbstractCrudDaoImpl<CourseEntity> implements CourseDao {

    private static final String SAVE_QUERY
        = "INSERT INTO courses (course_name, course_description) values(?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM courses";
    private static final String UPDATE_QUERY
        = "UPDATE courses SET course_name =?, course_description=? WHERE course_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM courses WHERE course_id = ?";
    private static final String FIND_ALL_BY_PAGE_QUERY
        = "SELECT * FROM courses ORDER BY course_id DESC LIMIT ? OFFSET ?";

    public CourseDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, UPDATE_QUERY,
            DELETE_BY_ID_QUERY, FIND_ALL_BY_PAGE_QUERY);
    }

    @Override
    protected CourseEntity mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new CourseEntity(
            resultSet.getLong("course_id"),
            resultSet.getString("course_name"),
            resultSet.getString("course_description"));
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, CourseEntity entity)
        throws SQLException {
        preparedStatement.setString(1, entity.courseName());
        preparedStatement.setString(2, entity.courseDescription());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, CourseEntity oldEntity, CourseEntity newEntity)
        throws SQLException {
        preparedStatement.setString(1, newEntity.courseName());
        preparedStatement.setString(2, newEntity.courseDescription());
        preparedStatement.setLong(3, oldEntity.courseId());
    }

    @Override
    protected void deleteById(PreparedStatement preparedStatement, Long id) throws SQLException {
        preparedStatement.setLong(1, id);
    }
}
