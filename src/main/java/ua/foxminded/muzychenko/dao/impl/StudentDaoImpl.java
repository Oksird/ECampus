package ua.foxminded.muzychenko.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.StudentEntity;

public class StudentDaoImpl extends AbstractCrudDaoImpl<StudentEntity> implements StudentDao {

    private static final String SAVE_QUERY
        = "INSERT INTO students (group_id, first_name ,last_name) values(?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM students WHERE student_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM students";
    private static final String UPDATE_QUERY
        = "UPDATE students SET group_id =?, first_name=?, last_name=? WHERE student_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM students WHERE student_id = ?";
    private static final String FIND_BY_COURSE_QUERY
        = "SELECT * FROM students st JOIN student_courses sc on st.student_id = sc.student_id "
        + "Join courses c on c.course_id = sc.course_id WHERE c.course_name = ?";
    private static final String ADD_TO_COURSE_QUERY
        =
        "INSERT INTO student_courses (student_id, course_id)"
            + " SELECT s.student_id, c.course_id FROM students s"
            + " JOIN (SELECT course_id FROM courses WHERE course_name = ?) c ON true"
            + " WHERE s.student_id = ?;";


    public StudentDaoImpl(DBConnector connector) {
        super(connector, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, UPDATE_QUERY,
            DELETE_BY_ID_QUERY);
    }

    @Override
    public Optional<StudentEntity> findByCourse(String nameOfCourse) {
        return findByStringParam(nameOfCourse, FIND_BY_COURSE_QUERY);
    }

    @Override
    public void addToCourse(StudentEntity student, String nameOfCourse) {
        addSpecificData(ADD_TO_COURSE_QUERY, nameOfCourse, student.studentId());
    }

    @Override
    protected StudentEntity mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new StudentEntity(
            resultSet.getLong("student_id"),
            resultSet.getLong("group_id"),
            resultSet.getString("first_name"),
            resultSet.getString("last_name"));
    }

    @Override
    protected void insert(PreparedStatement preparedStatement, StudentEntity entity)
        throws SQLException {
        preparedStatement.setLong(1, entity.groupId());
        preparedStatement.setString(2, entity.firstName());
        preparedStatement.setString(3, entity.lastName());
    }

    @Override
    protected void updateValues(PreparedStatement preparedStatement, StudentEntity entity)
        throws SQLException {
        insert(preparedStatement, entity);
        preparedStatement.setLong(4, entity.studentId());
    }

    @Override
    protected void deleteById(PreparedStatement preparedStatement, Long id) throws SQLException {
        preparedStatement.setLong(1, id);
    }

}
