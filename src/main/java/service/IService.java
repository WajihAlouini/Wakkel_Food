package service;

import entities.client;
import java.util.List;

public interface IService <T>{

    void add(String nom, String prenom, String email, String numero, String adresse, String mdp, client.GenreEnum genre, String image);
    void delete(String email);

    void update(T t);

    List<T> readAll();
    boolean validelogin(String email, String password);
}
