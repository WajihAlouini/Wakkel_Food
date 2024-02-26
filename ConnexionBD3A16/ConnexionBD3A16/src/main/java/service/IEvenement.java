
package service;

        import entities.Evenement;

        import java.util.List;

public interface IEvenement <E>{

    void supprimer(E e);

    void modifier(E e);

    void supprimer(int id);

    void ajouter(E e);
    void Modifier(E e);



    List<E> readAll();

    E readById(int id);
}