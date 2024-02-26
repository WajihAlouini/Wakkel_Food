package gestion_reclamation.service;

import java.util.List;

public interface IService<T> {

    void add(T t);

    void delete(int id);

    void update(T t);

    List<T> readAll();

    T readById(int id);
}