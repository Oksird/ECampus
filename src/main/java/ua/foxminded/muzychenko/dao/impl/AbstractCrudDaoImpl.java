package ua.foxminded.muzychenko.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.muzychenko.dao.CrudDao;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E, Long> {

    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<E> rowMapper;
    private final String createQuery;
    private final String updateQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String deleteByIdQuery;

    protected AbstractCrudDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<E> rowMapper, String createQuery, String updateQuery,
                                  String findByIdQuery, String findAllQuery, String deleteByIdQuery) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
        this.createQuery = createQuery;
        this.updateQuery = updateQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void create(E entity) {
        jdbcTemplate.update(createQuery, getCreateParameters(entity));
    }

    @Override
    public void update(Long id, E newEntity) {
        jdbcTemplate.update(updateQuery, getUpdateParameters(id, newEntity));
    }

    @Override
    public Optional<E> findById(Long id) {
        try {
            E result = jdbcTemplate.queryForObject(findByIdQuery, new Object[]{id}, rowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<E> findAll() {
        return jdbcTemplate.query(findAllQuery, rowMapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(deleteByIdQuery, id);
    }

    @Override
    public List<E> findAllByPage(Long pageNumber, Long pageSize) {
        long offset = (pageNumber - 1) * pageSize;
        String query = findAllQuery + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, rowMapper, pageSize, offset);
    }

    protected abstract Object[] getCreateParameters(E entity);

    protected abstract Object[] getUpdateParameters(Long id, E newEntity);

}
