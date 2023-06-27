package ua.foxminded.muzychenko.dao;

import java.util.List;

public interface DataGenerator<E> {

    List<E> generateData();
}
