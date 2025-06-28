package ru.msu.cmc.webprack.DAO;

import java.util.Collection;
public interface CommonDAO<T, ID> {
    T getById(ID id);

    Collection<T> getAll();
    void save(T entity);

    //void saveCollection(Collection<T> entities);

    void delete(T entity);

    //void deleteById(ID id);

    void update(T entity);
}
