package ua.foxminded.muzychenko.dao.data;

import java.util.List;

public interface DataGenerator<E> {

    List<E> generateData();
}
