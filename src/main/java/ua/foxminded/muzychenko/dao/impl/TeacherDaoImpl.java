package ua.foxminded.muzychenko.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.TeacherDao;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TeacherDaoImpl extends AbstractCrudDaoImpl<Teacher> implements TeacherDao {

    private static final String FIND_ALL_QUERY = "FROM Teacher";
    private static final String FIND_BY_COURSE_QUERY = """
        SELECT t
        FROM Teacher t
        JOIN t.courses c
        WHERE c.courseName = :courseName
        """;
    private static final String FIND_COURSE_TO_ADD_TEACHER_QUERY = """
        FROM Course c
        WHERE c.courseName = :courseName
        """;
    private static final String FIND_BY_EMAIL_QUERY = """
        FROM Teacher t
        WHERE t.email = :email
        """;

    protected TeacherDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, FIND_ALL_QUERY, Teacher.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Teacher> findByCourse(String courseName) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(FIND_BY_COURSE_QUERY, Teacher.class)
            .setParameter("courseName", courseName)
            .getResultList();
    }

    @Transactional
    @Override
    public void addToCourse(UUID id, String courseName) {
        Session session = sessionFactory.getCurrentSession();

        Teacher teacher = session.get(Teacher.class, id);
        Course course = session.createQuery(FIND_COURSE_TO_ADD_TEACHER_QUERY, Course.class)
            .setParameter("courseName", courseName)
            .getSingleResult();

        teacher.getCourses().add(course);
    }

    @Transactional
    @Override
    public void excludeFromCourse(UUID id, String courseName) {
        Session session = sessionFactory.getCurrentSession();

        Teacher teacher = session.get(Teacher.class, id);
        Course course = session.createQuery(FIND_COURSE_TO_ADD_TEACHER_QUERY, Course.class)
            .setParameter("courseName", courseName)
            .getSingleResult();

        teacher.getCourses().remove(course);
    }

    @Transactional
    @Override
    public Optional<Teacher> findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();

        Teacher teacher = session.createQuery(FIND_BY_EMAIL_QUERY, Teacher.class)
            .setParameter("email", email)
            .getSingleResult();

        return Optional.of(teacher);
    }

    @Override
    protected void executeUpdateEntity(Session session, UUID id, Teacher newEntity) {
        Teacher teacher = session.get(Teacher.class, id);

        teacher.setFirstName(newEntity.getFirstName());
        teacher.setLastName(newEntity.getLastName());
        teacher.setEmail(newEntity.getEmail());
        teacher.setPassword(newEntity.getPassword());

        session.merge(teacher);
    }
}
