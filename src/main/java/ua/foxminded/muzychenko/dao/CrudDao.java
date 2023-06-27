package ua.foxminded.muzychenko.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<E, ID> {

    void create(E entity);

    Optional<E> findById(ID id);

    List<E> findAll();

    List<E> findAllByPage(Long countOfPages, Long pageSize);

    void update(E oldEntity, E newEntity);

    void deleteById(ID id);

    void createAll(List<E> entities);
}
