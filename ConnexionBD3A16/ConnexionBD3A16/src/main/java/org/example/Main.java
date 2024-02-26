package org.example;

import entities.Evenement;
import service.*;

import java.sql.Date;
public class Main {



    public static void main(String[] args) {



IEvenement<Evenement> es= new EvenementService();
//Evenement newEvent = new Evenement(2,"uuyg",Date.valueOf("2024-02-24"),Date.valueOf("2024-02-30") );

//ADD
       // es.ajouter(newEvent);

//affichage
       // System.out.println( es.readAll());
//Delete

      //  es.supprimer(4);
//Update
        //Produit p =new Produit(7,"creatine",3,100,"behi",2);
       //  ((EvenementService) es).modifier(newEvent);
        }
}
