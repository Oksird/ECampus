package ua.foxminded.muzychenko.dao.impl;

import ua.foxminded.muzychenko.DBConnector;
import ua.foxminded.muzychenko.dao.CrudDao;
import ua.foxminded.muzychenko.exception.DataBaseRunTimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E, Long> {

    private static final BiConsumer<PreparedStatement, String> STRING_CONSUMER =
        (PreparedStatement preparedStatement, String param) -> {
            try {
                preparedStatement.setString(1, param);
            } catch (SQLException e) {
                throw new DataBaseRunTimeException(e);
            }
        };

    private static final BiConsumer<PreparedStatement, Long> LONG_CONSUMER =
        (PreparedStatement preparedStatement, Long param) -> {
            try {
                preparedStatement.setLong(1, param);
            } catch (SQLException e) {
                throw new DataBaseRunTimeException(e);
            }
        };

    private final DBConnector connector;
    private final String createQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;
    private final String findAllByPageQuery;

    protected AbstractCrudDaoImpl(DBConnector connector, String createQuery, String findByIdQuery,
        String findAllQuery, String updateQuery, String deleteByIdQuery,
        String findAllByPageQuery) {
        this.connector = connector;
        this.createQuery = createQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.updateQuery = updateQuery;
        this.deleteByIdQuery = deleteByIdQuery;
        this.findAllByPageQuery = findAllByPageQuery;
    }

    @Override
    public void create(E entity) {
        try (Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(createQuery)) {
            insert(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseRunTimeException("Insertion is failed", e);
        }
    }

    @Override
    public Optional<E> findById(Long id) {
        return findByLongParam(id, findByIdQuery);
    }

    @Override
    public List<E> findAll() {
        try (Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                List<E> entities = new ArrayList<>();
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    }

    @Override
    public List<E> findAllByPage(Long pageNumber, Long pageSize) {
        List<E> listsOfEntities = new ArrayList<>();
        long offset = (pageNumber - 1) * pageSize;
        try (Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                findAllByPageQuery)) {
            preparedStatement.setLong(1, pageSize);
            preparedStatement.setLong(2, offset);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    listsOfEntities.add(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
        return listsOfEntities;
    }

    @Override
    public void update(E oldEntity, E newEntity) {
        try (Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            updateValues(preparedStatement, oldEntity, newEntity);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteByIdQuery)) {

            deleteById(preparedStatement, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    }
    @Override
    public void createAll(List<E> entities) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(createQuery)) {
            for (E entity : entities) {
                insert(preparedStatement, entity);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new DataBaseRunTimeException("Batch insertion is failed", e);
        }
    }

    protected List<E> findAllByLongParam(Long id, String query) {
        return findAllByParam(id, query, LONG_CONSUMER);
    }

    protected Optional<E> findByLongParam(Long id, String query) {
        return findByParam(id, query, LONG_CONSUMER);
    }

    protected Optional<E> findByStringParam(String param, String query) {
        return findByParam(param, query, STRING_CONSUMER);
    }

    protected List<E> findAllByStringParam(String param, String query) {
        return findAllByParam(param, query, STRING_CONSUMER);
    }

    private <P> List<E> findAllByParam(P param, String query,
                                       BiConsumer<PreparedStatement, P> consumer) {
        List<E> results = new ArrayList<>();
        try (Connection connection = connector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            consumer.accept(preparedStatement, param);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    E entity = mapResultSetToEntity(resultSet);
                    if (entity != null) {
                        results.add(entity);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
        return results;
    }

    private <P> Optional<E> findByParam(P param, String query,
                                        BiConsumer<PreparedStatement, P> consumer) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            consumer.accept(preparedStatement, param);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.ofNullable(mapResultSetToEntity(resultSet))
                    : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    }


    protected void addSpecificData(String query, Object... params) {
        try (Connection connection = connector.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param instanceof String string) {
                    statement.setString(i + 1, string);
                } else if (param instanceof Long number) {
                    statement.setLong(i + 1, number);
                } else {
                    throw new IllegalArgumentException(
                        "Unsupported parameter type: " + param.getClass().getSimpleName());
                }
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseRunTimeException("Failed to execute statement: " + query, e);
        }
    }

    protected void removeSpecificData(String query, Object entityId, Object entityName) {
        try (Connection connection = connector.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, (Long) entityId);
            statement.setString(2, (String) entityName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseRunTimeException("Failed to execute statement: " + query, e);
        }
    }


    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void insert(PreparedStatement preparedStatement, E entity)
        throws SQLException;

    protected abstract void updateValues(PreparedStatement preparedStatement, E oldEntity, E newEntity)
        throws SQLException;

    protected abstract void deleteById(PreparedStatement preparedStatement, Long id)
        throws SQLException;
}
