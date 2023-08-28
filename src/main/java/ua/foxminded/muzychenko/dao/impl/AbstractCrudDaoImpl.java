package ua.foxminded.muzychenko.dao.impl;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.muzychenko.dao.CrudDao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E, UUID> {

    protected final SessionFactory sessionFactory;
    private final String findAllQuery;
    private final Class<E> entityClass;

    @Transactional
    @Override
    public void create(E entity) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);
    }

    @Transactional
    @Override
    public void update(UUID id, E newEntity) {
        Session session = sessionFactory.getCurrentSession();
        executeUpdateEntity(session, id, newEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<E> findById(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.of(session.get(entityClass, id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<E> findAll() {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(findAllQuery, entityClass).getResultList();
    }

    @Transactional
    @Override
    public void deleteById(UUID id) {
        Session session = sessionFactory.getCurrentSession();

        session.remove(session.get(entityClass, id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<E> findAll(Long pageNumber, Long pageSize) {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<E> root = criteriaQuery.from(entityClass);

        criteriaQuery.select(root);

        TypedQuery<E> query = session.createQuery(criteriaQuery);
        query.setFirstResult((int) ((pageNumber - 1) * pageSize));
        query.setMaxResults(Math.toIntExact(pageSize));

        return query.getResultList();
    }

    protected abstract void executeUpdateEntity(Session session, UUID id, E newEntity);
}
