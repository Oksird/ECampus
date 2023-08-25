package ua.foxminded.muzychenko.dao.impl;

import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.AdminDao;
import ua.foxminded.muzychenko.entity.Admin;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AdminDaoImpl extends AbstractCrudDaoImpl<Admin> implements AdminDao {

    private static final String FIND_ALL_QUERY ="from Admin";
    private static final String FIND_BY_EMAIL_QUERY = "from Admin a where a.email = :email";

    protected AdminDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, FIND_ALL_QUERY, Admin.class);
    }

    @SuppressWarnings("deprecation")
    @Transactional(readOnly = true)
    @Override
    public Optional<Admin> findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery(FIND_BY_EMAIL_QUERY);

        query.setParameter("email", email);

        return Optional.of((Admin) query.getSingleResult());
    }

    @Override
    protected void executeUpdateEntity(Session session, UUID id, Admin newEntity) {
        Admin admin = session.get(Admin.class, id);

        admin.setFirstName(newEntity.getFirstName());
        admin.setLastName(newEntity.getLastName());
        admin.setEmail(newEntity.getEmail());
        admin.setPassword(newEntity.getPassword());

        session.merge(admin);
    }
}
