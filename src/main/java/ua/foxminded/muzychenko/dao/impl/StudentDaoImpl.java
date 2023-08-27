package ua.foxminded.muzychenko.dao.impl;

import lombok.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.Student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentDaoImpl extends AbstractCrudDaoImpl<Student> implements StudentDao {

    private static final String CREATE_QUERY = "INSERT INTO users VALUES (?, 'Student', ?, ?, ? ,? )";
    private static final String UPDATE_QUERY = "UPDATE users SET first_name=?, last_name=?, email=?, password=? WHERE user_id=?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users WHERE user_type='Student'";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM users WHERE user_id=?";
    private static final String FIND_BY_COURSE_QUERY = """
        SELECT u.*
        FROM public.users AS u
        WHERE u.user_type = 'Student' AND u.user_id IN (
            SELECT student_id
            FROM public.students_courses sc
            JOIN public.courses c ON sc.course_id = c.course_id
            WHERE c.course_name = ?
        );
        """;
    private static final String FIND_BY_GROUP_QUERY = """
        SELECT u.*
        FROM public.users AS u
        JOIN public.groups AS g ON u.group_id = g.group_id
        WHERE u.user_type = 'Student' AND g.group_name =?;
        """;
    private static final String ADD_TO_COURSE_QUERY = """
        INSERT INTO public.students_courses (student_id, course_id)
        VALUES (?,
        (SELECT course_id
            FROM public.courses
            WHERE course_name =?));
        """;
    private static final String ADD_TO_GROUP_QUERY = """
        UPDATE public.users
        SET group_id = (
            SELECT group_id
            FROM public.groups
            WHERE group_name =?
        )
        WHERE user_id =? AND user_type = 'Student';
        """;
    private static final String DELETE_FROM_COURSE_QUERY = """
        DELETE FROM public.students_courses
        WHERE student_id = ? AND course_id IN (
            SELECT course_id
            FROM public.courses
            WHERE course_name = ?
        );
        """;
    private static final String DELETE_FROM_GROUP_QUERY = """
        WITH group_info AS (
            SELECT group_id
            FROM public.groups
            WHERE group_name = ?
        )
        UPDATE public.users AS u
        SET group_id = NULL
        FROM group_info
        WHERE u.user_id = ? AND u.user_type = 'Student' AND u.group_id = group_info.group_id;
        """;

    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email= ? AND user_type='Student'";

    protected StudentDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Student> studentRowMapper) {
        super(jdbcTemplate, studentRowMapper, CREATE_QUERY, UPDATE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, DELETE_BY_ID_QUERY);
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<Student> findByCourse(String nameOfCourse) {
        return jdbcTemplate.query(
            FIND_BY_COURSE_QUERY,
            new Object[]{nameOfCourse},
            rowMapper
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<Student> findByGroup(String nameOfGroup) {
        return jdbcTemplate.query(
            FIND_BY_GROUP_QUERY,
            new Object[]{nameOfGroup},
            rowMapper
        );
    }

    @Override
    public void addToCourse(UUID id, String courseName) {
        jdbcTemplate.update(
            ADD_TO_COURSE_QUERY,
            id,
            courseName
        );
    }

    @Override
    public void addToGroup(UUID id, String groupName) {
        jdbcTemplate.update(
            ADD_TO_GROUP_QUERY,
            groupName,
            id
        );
    }

    @Override
    public void excludeFromCourse(UUID id, String nameOfCourse) {
        jdbcTemplate.update(
            DELETE_FROM_COURSE_QUERY,
            id,
            nameOfCourse
        );
    }

    @Override
    public void excludeFromGroup(UUID id, String nameOfGroup) {
        jdbcTemplate.update(
            DELETE_FROM_GROUP_QUERY,
            nameOfGroup,
            id
        );
    }

    @Override
    protected Object[] getCreateParameters(@NonNull Student entity) {
        return new Object[]{
            entity.getUserId(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPassword()
        };
    }

    @Override
    protected Object[] getUpdateParameters(UUID id, Student newEntity) {
        return new Object[]{
            newEntity.getFirstName(),
            newEntity.getLastName(),
            newEntity.getEmail(),
            newEntity.getPassword(),
            id
        };
    }

    @Override
    public Optional<Student> findByEmail(String email) {
        return findByParam(FIND_BY_EMAIL_QUERY, email);
    }
}
