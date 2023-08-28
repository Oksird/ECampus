package ua.foxminded.muzychenko.dao.impl;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.entity.Course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CourseDaoImpl extends AbstractCrudDaoImpl<Course> implements CourseDao {

    private static final String FIND_ALL_QUERY = "from Course";
    private static final String FIND_NAME_QUERY = "from Course c where c.courseName = :name";
    private static final String FIND_COURSE_BY_USER_AND_TYPE_QUERY = """
        SELECT DISTINCT c
        FROM Course c
        LEFT JOIN c.students s
        LEFT JOIN c.teachers t
        WHERE s.userId = :userId OR t.userId = :userId
        """;
    private static final Class<Course> COURSE_CLASS = Course.class;

    protected CourseDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, FIND_ALL_QUERY, COURSE_CLASS);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Course> findCoursesByUserIdAndUserType(UUID userId) {
        Session session = sessionFactory.getCurrentSession();

        TypedQuery<Course> query = session.createQuery(FIND_COURSE_BY_USER_AND_TYPE_QUERY, COURSE_CLASS);

        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Course> findByName(String courseName) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery(FIND_NAME_QUERY, COURSE_CLASS);

        query.setParameter("name", courseName);

        return Optional.of((Course) query.getSingleResult());
    }

    @Override
    protected void executeUpdateEntity(Session session, UUID id, Course newEntity) {
        Course course = session.get(COURSE_CLASS, id);

        course.setCourseName(newEntity.getCourseName());
        course.setCourseDescription(newEntity.getCourseDescription());

        session.merge(course);
    }
}
