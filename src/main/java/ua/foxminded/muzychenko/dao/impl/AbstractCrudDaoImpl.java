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
    private static final BiConsumer<PreparedStatement, String> STRING_CONSUMER
        = (PreparedStatement preparedStatement, String param) -> {
        try {
            preparedStatement.setString(1, param);
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    };

    private static final BiConsumer<PreparedStatement, Long> LONG_CONSUMER
        = (PreparedStatement preparedStatement, Long param) -> {
        try {
            preparedStatement.setLong(1, param);
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    };

    private final DBConnector connector;
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    protected AbstractCrudDaoImpl(DBConnector connector, String saveQuery, String findByIdQuery,
                               String findAllQuery, String updateQuery, String deleteByIdQuery) {
        this.connector = connector;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.updateQuery = updateQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void create(E entity) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {

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
    public void update(E entity) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            updateValues(preparedStatement, entity);

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

    protected Optional<E> findByLongParam(Long id, String query) {
        return findByParam(id, query, LONG_CONSUMER);
    }

    protected Optional<E> findByStringParam(String param, String query) {
        return findByParam(param, query, STRING_CONSUMER);
    }

    private <P> Optional<E> findByParam(P param, String query, BiConsumer<PreparedStatement, P> consumer) {
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            consumer.accept(preparedStatement, param);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.ofNullable(mapResultSetToEntity(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataBaseRunTimeException(e);
        }
    }

    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void insert(PreparedStatement preparedStatement, E entity) throws SQLException;

    protected abstract void updateValues(PreparedStatement preparedStatement, E entity) throws SQLException;

    protected abstract void deleteById(PreparedStatement preparedStatement, Long id) throws SQLException;

}
