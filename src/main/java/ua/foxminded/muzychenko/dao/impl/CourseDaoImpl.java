package ua.foxminded.muzychenko.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CourseDaoImpl extends AbstractCrudDaoImpl<Course> implements CourseDao {

    private static final String CREATE_QUERY = "INSERT INTO courses VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE courses SET course_name=?, course_description=? WHERE course_id=?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM courses";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM courses WHERE course_id=?";
    private static final String FIND_STUDENT_COURSES_QUERY = """
        SELECT c.*
        FROM courses c
        JOIN students_courses sc ON c.course_id = sc.course_id
        JOIN users u ON sc.student_id = u.user_id
        WHERE u.user_id =?;
        """;
    private static final String FIND_TEACHER_COURSES_QUERY = """
        SELECT c.*
        FROM courses c
        JOIN teachers_courses tc ON c.course_id = tc.course_id
        WHERE tc.teacher_id = ?;
        """;

    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM courses WHERE course_name = ?";

    @Autowired
    protected CourseDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Course> rowMapper) {
        super(jdbcTemplate, rowMapper, CREATE_QUERY, UPDATE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    protected Object[] getCreateParameters(Course course) {
        return new Object[]{course.getCourseId(), course.getCourseName(), course.getCourseDescription()};
    }

    @Override
    protected Object[] getUpdateParameters(UUID id, Course updatedCourse) {
        return new Object[]{updatedCourse.getCourseName(), updatedCourse.getCourseDescription(), id};
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<Course> findCoursesByUserIdAndUserType(UUID userId, UserType userType) {
        String query = switch (userType) {
            case STUDENT -> FIND_STUDENT_COURSES_QUERY;
            case TEACHER -> FIND_TEACHER_COURSES_QUERY;
            case ADMIN -> throw new IllegalArgumentException("Unsupported user type: Admin cannot access courses");
        };

        return jdbcTemplate.query(
            query,
            new Object[]{userId},
            rowMapper
        );
    }

    @Override
    public Optional<Course> findByName(String courseName) {
        return findByParam(FIND_BY_NAME_QUERY, courseName);
    }

}
