package org.edu.dao.common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ILNUR on 29.10.2016.
 */
public interface IOperations<T extends Serializable> {

    T findOne(final long id);

    List<T> findAll();

    void create(final T entity);

    T update(final T entity);

    void delete(final T entity);

    void deleteById(final long entityId);
}
