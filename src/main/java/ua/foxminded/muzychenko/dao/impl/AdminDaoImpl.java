package ua.foxminded.muzychenko.dao.impl;

import lombok.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.dao.AdminDao;
import ua.foxminded.muzychenko.entity.Admin;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AdminDaoImpl extends AbstractCrudDaoImpl<Admin> implements AdminDao {

    private static final String CREATE_QUERY = "INSERT INTO users VALUES (?, 'Admin', ?, ?, ? ,? )";
    private static final String UPDATE_QUERY = "UPDATE users SET first_name=?, last_name=?, email=?, password=? WHERE user_id=?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users WHERE user_type='Admin'";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM users WHERE user_id=?";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email= ? AND user_type='Admin'";

    protected AdminDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Admin> rowMapper) {
        super(jdbcTemplate, rowMapper, CREATE_QUERY, UPDATE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    protected Object[] getCreateParameters(@NonNull Admin entity) {
        return new Object[]{
            entity.getUserId(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPassword()
        };
    }

    @Override
    protected Object[] getUpdateParameters(UUID id, @NonNull Admin newEntity) {
        return new Object[]{
            newEntity.getFirstName(),
            newEntity.getLastName(),
            newEntity.getEmail(),
            newEntity.getPassword(),
            id
        };
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return findByParam(FIND_BY_EMAIL_QUERY, email);
    }
}
