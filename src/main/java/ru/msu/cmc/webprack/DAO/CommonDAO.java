package ru.msu.cmc.webprack.DAO;

import java.util.Collection;
public interface CommonDAO<T, ID> {
    T getById(ID id);

    Collection<T> getAll();
    void save(T entity);

    void delete(T entity);

    void update(T entity);
}
