package org.example.test;
import entities.*;

import Services.*;
import entities.Plat;


public class Main {

    public static void main(String[] args) {

        CrudService<Plat> platService = new PlatService();

        // Adding a new Plat
        Plat newPlat = new Plat();
        newPlat.setNomPlat("Plat 1");
        newPlat.setPrix(15.99);
        newPlat.setIngredient("Couscous with vegetables");
        // Assuming you have a Restaurant object with ID 2
        Restaurant restaurant = new Restaurant();
        restaurant.setId_restaurant(2);
        newPlat.setRestaurant(restaurant);

        platService.add(newPlat);
        System.out.println("Added Plat: " + newPlat);

        // Displaying all Plats
        System.out.println("All Plats:");
        platService.readAll().forEach(System.out::println);

        // Updating a Plat
        Plat updatedPlat = new Plat();
        updatedPlat.setId_plat(1);
        updatedPlat.setNomPlat("Plat 1 modified");
        updatedPlat.setPrix(18.99);
        updatedPlat.setIngredient("Couscous with special ingredients");
        // Assuming you have a Restaurant object with ID 2
        updatedPlat.setRestaurant(restaurant);

        platService.update(updatedPlat);
        System.out.println("Updated Plat with ID 1");

        // Deleting a Plat
        platService.delete(1);
        System.out.println("Deleted Plat with ID 1");

        // Displaying all Plats after modifications
        System.out.println("All Plats after modifications:");
        platService.readAll().forEach(System.out::println);
    }
}
