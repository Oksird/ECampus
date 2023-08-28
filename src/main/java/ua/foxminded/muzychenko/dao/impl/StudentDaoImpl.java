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
    private static final Class<Student> STUDENT_CLASS = Student.class;
    private static final Class<Group> GROUP_CLASS = Group.class;
    private static final Class<Course> COURSE_CLASS = Course.class;
    private static final String COURSE_NAME_PARAMETER = "courseName";

    protected StudentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, FIND_ALL_QUERY, STUDENT_CLASS);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> findByCourse(String nameOfCourse) {
        Session session = sessionFactory.getCurrentSession();
        return session
            .createQuery(FIND_BY_COURSE_QUERY, STUDENT_CLASS)
            .setParameter(COURSE_NAME_PARAMETER, nameOfCourse)
            .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> findByGroup(String nameOfGroup) {
        Session session = sessionFactory.getCurrentSession();

        return session
            .createQuery(FIND_BY_GROUP_QUERY, STUDENT_CLASS)
            .setParameter("groupName", nameOfGroup)
            .getResultList();
    }

    @Transactional
    @Override
    public void addToCourse(UUID id, String courseName) {
        Session session = sessionFactory.getCurrentSession();

        Student student = session.get(STUDENT_CLASS, id);
        Course course = session.createQuery(FIND_COURSE_TO_ADD_STUDENT_QUERY, COURSE_CLASS)
            .setParameter(COURSE_NAME_PARAMETER, courseName)
            .getSingleResult();

        student.getCourses().add(course);
    }

    @Transactional
    @Override
    public void addToGroup(UUID id, String groupName) {
        Session session = sessionFactory.getCurrentSession();

        Student student = session.get(STUDENT_CLASS, id);
        Group group = session
            .createQuery(FIND_GROUP_TO_ADD_STUDENT_QUERY, GROUP_CLASS)
            .setParameter("groupName", groupName)
            .getSingleResult();
        student.setGroup(group);
    }

    @Transactional
    @Override
    public void excludeFromCourse(UUID id, String nameOfCourse) {
        Session session = sessionFactory.getCurrentSession();

        Student student = session.get(STUDENT_CLASS, id);
        Course course = session.createQuery(FIND_COURSE_TO_ADD_STUDENT_QUERY, COURSE_CLASS)
            .setParameter(COURSE_NAME_PARAMETER, nameOfCourse)
            .getSingleResult();

        student.getCourses().remove(course);
    }

    @Transactional
    @Override
    public void excludeFromGroup(UUID id) {
        Session session = sessionFactory.getCurrentSession();

        Student student = session.get(STUDENT_CLASS, id);
        student.setGroup(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Student> findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();

        Student student = session.createQuery(FIND_BY_EMAIL_QUERY, STUDENT_CLASS)
            .setParameter("email", email)
            .getSingleResult();

        return Optional.of(student);
    }

    @Override
    protected void executeUpdateEntity(Session session, UUID id, Student newEntity) {
        Student student = session.get(STUDENT_CLASS, id);

        student.setFirstName(newEntity.getFirstName());
        student.setLastName(newEntity.getLastName());
        student.setEmail(newEntity.getEmail());
        student.setPassword(newEntity.getPassword());

        session.merge(student);
    }
}
