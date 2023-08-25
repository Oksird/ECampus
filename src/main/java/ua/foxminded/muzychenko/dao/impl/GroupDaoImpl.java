package ua.foxminded.muzychenko.dao.impl;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.GroupDao;
import ua.foxminded.muzychenko.entity.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GroupDaoImpl extends AbstractCrudDaoImpl<Group> implements GroupDao {

    private static final String FIND_ALL_QUERY = "from Group";
    private static final String FIND_NAME_QUERY = "from Group g where g.groupName = :name";
    private static final String FIND_GROUP_WITH_LESS_OR_EQUAL_STUDENTS_QUERY = """
        SELECT g
        FROM Group g
        WHERE size(g.students) <= :maxStudents
    """;
    private static final String FIND_USER_GROUP_QUERY = """
        SELECT g
        FROM Group g
        JOIN g.students s
        WHERE s.id = :userId
    """;

    protected GroupDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, FIND_ALL_QUERY, Group.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Group> findGroupWithLessOrEqualStudents(Integer countOfStudents) {
        Session session = sessionFactory.getCurrentSession();
        return session
            .createQuery(FIND_GROUP_WITH_LESS_OR_EQUAL_STUDENTS_QUERY, Group.class)
            .setParameter("maxStudents", countOfStudents)
            .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Group> findUsersGroup(UUID userId) {
        Session session = sessionFactory.getCurrentSession();

        return Optional.of(
            session
                .createQuery(FIND_USER_GROUP_QUERY, Group.class)
                .setParameter("userId", userId)
                .getSingleResult()
        );
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("deprecation")
    @Override
    public Optional<Group> findByName(String groupName) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery(FIND_NAME_QUERY);

        query.setParameter("name", groupName);

        return Optional.of((Group) query.getSingleResult());
    }

    @Override
    protected void executeUpdateEntity(Session session, UUID id, Group newEntity) {
        Group group = session.get(Group.class, id);

        group.setGroupName(newEntity.getGroupName());

        session.merge(group);
    }
}
