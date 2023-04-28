package ua.foxminded.muzychenko.dao.impl;

import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.StudentEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StudentDaoImpl extends AbstractCrudDaoImpl<StudentEntity> implements StudentDao {

    private static final String SAVE_QUERY = "INSERT INTO students (group_id, first_name ,last_name) values(?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM students WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM students";
    private static final String UPDATE_QUERY = "UPDATE students SET group_id =?, first_name=?, last_name=? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM students WHERE id = ?";
    private static final String FIND_BY_COURSE_QUERY
        = "SELECT * FROM students JOIN courses ON students.course_id = courses.id WHERE courses.name = ?";

    public StudentDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public Optional<StudentEntity> findByCourse(String nameOfCourse) {
        return findByStringParam(nameOfCourse, FIND_BY_COURSE_QUERY);
    }

    @Override
    protected StudentEntity mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new StudentEntity(
            resultSet.getLong("id"),
            resultSet.getLong("group_id"),
            resultSet.getString("first_name"),
            resultSet.getString("last_name"));
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, StudentEntity entity) throws SQLException {
        preparedStatement.setLong(2, entity.groupId());
        preparedStatement.setString(3, entity.firstName());
        preparedStatement.setString(4, entity.lastName());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, StudentEntity entity) throws SQLException {
        insert(preparedStatement, entity);
        preparedStatement.setLong(1, entity.studentId());
    }

    @Override
    protected void deleteById(PreparedStatement preparedStatement, Long id) throws SQLException {
        preparedStatement.setLong(1, id);
    }

}
