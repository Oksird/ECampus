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

    protected CourseDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, FIND_ALL_QUERY, Course.class);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings({"deprecation", "unchecked"})
    @Override
    public List<Course> findCoursesByUserIdAndUserType(UUID userId) {
        Session session = sessionFactory.getCurrentSession();

        TypedQuery<Course> query = session.createQuery(FIND_COURSE_BY_USER_AND_TYPE_QUERY);

        query.setParameter("userId", userId);

        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("deprecation")
    @Override
    public Optional<Course> findByName(String courseName) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery(FIND_NAME_QUERY);

        query.setParameter("name", courseName);

        return Optional.of((Course) query.getSingleResult());
    }

    @Override
    protected void executeUpdateEntity(Session session, UUID id, Course newEntity) {
        Course course = session.get(Course.class, id);

        course.setCourseName(newEntity.getCourseName());
        course.setCourseDescription(newEntity.getCourseDescription());

        session.merge(course);
    }
}
