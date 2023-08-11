package ua.foxminded.muzychenko.dao;

import java.util.Optional;

public interface UserDao<E, ID> extends CrudDao<E, ID> {
    Optional<E> findByEmail(String email);
}
