package ua.foxminded.muzychenko.dao.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.foxminded.muzychenko.dao.CrudDao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E, UUID> {

    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<E> rowMapper;
    private final String createQuery;
    private final String updateQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String deleteByIdQuery;

    @Override
    public void create(E entity) {
        jdbcTemplate.update(createQuery, getCreateParameters(entity));
    }

    @Override
    public void update(UUID id, E newEntity) {
        jdbcTemplate.update(updateQuery, getUpdateParameters(id, newEntity));
    }

    @Override
    public Optional<E> findById(UUID id) {
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
    public void deleteById(UUID id) {
        jdbcTemplate.update(deleteByIdQuery, id);
    }

    @Override
    public List<E> findAllByPage(Long pageNumber, Long pageSize) {
        long offset = (pageNumber - 1) * pageSize;
        String query = findAllQuery + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, rowMapper, pageSize, offset);
    }

    protected abstract Object[] getCreateParameters(E entity);

    protected abstract Object[] getUpdateParameters(UUID id, E newEntity);

}
