package ua.foxminded.muzychenko.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.StudentDao;
import ua.foxminded.muzychenko.entity.Course;
import ua.foxminded.muzychenko.entity.Group;
import ua.foxminded.muzychenko.entity.Student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentDaoImpl extends AbstractCrudDaoImpl<Student> implements StudentDao {

    private static final String FIND_ALL_QUERY = "from Student";
    private static final String FIND_BY_COURSE_QUERY = """
        SELECT s
        FROM Student s
        JOIN s.courses c
        WHERE c.courseName = :courseName
        """;
    private static final String FIND_BY_GROUP_QUERY = """
        SELECT s
        FROM Student s
        JOIN s.group g
        WHERE g.groupName = :groupName
        """;
    private static final String FIND_COURSE_TO_ADD_STUDENT_QUERY = """
        FROM Course c
        WHERE c.courseName = :courseName
        """;
    private static final String FIND_GROUP_TO_ADD_STUDENT_QUERY = """
        FROM Group g
        WHERE g.groupName =:groupName
        """;
    private static final String FIND_BY_EMAIL_QUERY = "FROM Student s WHERE s.email =:email";

    protected StudentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, FIND_ALL_QUERY, Student.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> findByCourse(String nameOfCourse) {
        Session session = sessionFactory.getCurrentSession();
        return session
            .createQuery(FIND_BY_COURSE_QUERY, Student.class)
            .setParameter("courseName", nameOfCourse)
            .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> findByGroup(String nameOfGroup) {
        Session session = sessionFactory.getCurrentSession();

        return session
            .createQuery(FIND_BY_GROUP_QUERY, Student.class)
            .setParameter("groupName", nameOfGroup)
            .getResultList();
    }

    @Transactional
    @Override
    public void addToCourse(UUID id, String courseName) {
        Session session = sessionFactory.getCurrentSession();

        Student student = session.get(Student.class, id);
        Course course = session.createQuery(FIND_COURSE_TO_ADD_STUDENT_QUERY, Course.class)
            .setParameter("courseName", courseName)
            .getSingleResult();

        student.getCourses().add(course);
    }

    @Transactional
    @Override
    public void addToGroup(UUID id, String groupName) {
        Session session = sessionFactory.getCurrentSession();

        Student student = session.get(Student.class, id);
        Group group = session
            .createQuery(FIND_GROUP_TO_ADD_STUDENT_QUERY, Group.class)
            .setParameter("groupName", groupName)
            .getSingleResult();
        student.setGroup(group);
    }

    @Transactional
    @Override
    public void excludeFromCourse(UUID id, String nameOfCourse) {
        Session session = sessionFactory.getCurrentSession();

        Student student = session.get(Student.class, id);
        Course course = session.createQuery(FIND_COURSE_TO_ADD_STUDENT_QUERY, Course.class)
            .setParameter("courseName", nameOfCourse)
            .getSingleResult();

        student.getCourses().remove(course);
    }

    @Transactional
    @Override
    public void excludeFromGroup(UUID id) {
        Session session = sessionFactory.getCurrentSession();

        Student student = session.get(Student.class, id);
        student.setGroup(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Student> findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();

        Student student = session.createQuery(FIND_BY_EMAIL_QUERY, Student.class)
            .setParameter("email", email)
            .getSingleResult();

        return Optional.of(student);
    }

    @Override
    protected void executeUpdateEntity(Session session, UUID id, Student newEntity) {
        Student student = session.get(Student.class, id);

        student.setFirstName(newEntity.getFirstName());
        student.setLastName(newEntity.getLastName());
        student.setEmail(newEntity.getEmail());
        student.setPassword(newEntity.getPassword());

        session.merge(student);
    }
}
