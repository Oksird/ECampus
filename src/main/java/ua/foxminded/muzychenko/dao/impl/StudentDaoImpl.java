package ua.foxminded.muzychenko.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.StudentEntity;

import java.util.List;

@Repository
public class StudentDaoImpl extends AbstractCrudDaoImpl<StudentEntity> implements StudentDao {

    private static final String CREATE_QUERY = "INSERT INTO students VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE students SET group_id=?, first_name=?, last_name=? WHERE student_id=?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM students WHERE student_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM students";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM students WHERE student_id=?";
    private static final String FIND_BY_COURSE_QUERY = """
        SELECT * FROM students st JOIN student_courses sc on st.student_id = sc.student_id
        JOIN courses c on c.course_id = sc.course_id WHERE c.course_name = ?
        """;
    private static final String ADD_TO_COURSE_QUERY = """
        INSERT INTO student_courses (student_id, course_id)
        SELECT s.student_id, c.course_id
        FROM students s
        JOIN (SELECT course_id FROM courses WHERE course_name = ?) c ON true
        WHERE s.student_id = ?
        AND NOT EXISTS (SELECT 1 FROM student_courses
        WHERE student_id = s.student_id AND course_id = c.course_id)
        """;
    private static final String DELETE_FROM_COURSE_QUERY = """
        DELETE FROM student_courses
        WHERE student_id = ?
        AND course_id = (SELECT course_id FROM courses WHERE course_name = ?)
        """;

    @Autowired
    public StudentDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<StudentEntity> rowMapper) {
        super(jdbcTemplate, rowMapper, CREATE_QUERY, UPDATE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<StudentEntity> findByCourse(String nameOfCourse) {
        return jdbcTemplate.query(
            FIND_BY_COURSE_QUERY,
            new Object[]{nameOfCourse},
            rowMapper
        );
    }

    @Override
    public void addToCourse(String nameOfCourse, Long id) {
        jdbcTemplate.update(ADD_TO_COURSE_QUERY, nameOfCourse, id);
    }

    @Override
    public void deleteFromCourse(Long id, String nameOfCourse) {
        jdbcTemplate.update(DELETE_FROM_COURSE_QUERY, id, nameOfCourse);
    }

    @Override
    protected Object[] getCreateParameters(StudentEntity entity) {
        return new Object[]{entity.studentId(), entity.groupId(), entity.firstName(), entity.lastName()};
    }

    @Override
    protected Object[] getUpdateParameters(Long id, StudentEntity newEntity) {
        return new Object[]{newEntity.groupId(), newEntity.firstName(), newEntity.lastName(), id};
    }

}
