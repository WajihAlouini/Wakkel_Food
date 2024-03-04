package service;
import entities.reclamation;
import entities.reclamation;

import java.io.InputStream;
import java.util.List;

public interface IServiceReclamation<T> {


    void add(reclamation r, InputStream imageInputStream);

    void add(T t);

    void delete(int id);

    void update(T t);

    List<T> readAll();

    T readById(int id);
}