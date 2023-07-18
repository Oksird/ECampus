package ua.foxminded.muzychenko.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<E, ID> {

    void create(E entity);

    void update(ID id, E newEntity);

    Optional<E> findById(ID id);

    List<E> findAll();

    void deleteById(ID id);

    List<E> findAllByPage(Long pageNumber, Long pageSize);

}
