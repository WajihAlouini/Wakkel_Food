package entities;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Restaurant {
    private int id_restaurant;
    private RestaurantCategory restaurantCategory; // Use RestaurantCategory object instead of id_category
    private String nom_restaurant;
    private String adresse_restaurant;
    private String imgSrc;

    public Restaurant() {
    }

    public Restaurant(RestaurantCategory restaurantCategory, String nom_restaurant, String adresse_restaurant,String imgSrc) {
        this.restaurantCategory = restaurantCategory;
        this.nom_restaurant = nom_restaurant;
        this.adresse_restaurant = adresse_restaurant;
        this.imgSrc=imgSrc;
    }

    public Restaurant(int id_restaurant, RestaurantCategory restaurantCategory, String nom_restaurant, String adresse_restaurant,String imgSrc) {
        this.id_restaurant = id_restaurant;
        this.restaurantCategory = restaurantCategory;
        this.nom_restaurant = nom_restaurant;
        this.adresse_restaurant = adresse_restaurant;
        this.imgSrc=imgSrc;
    }

    public int getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(int id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public RestaurantCategory getRestaurantCategory() {
        return restaurantCategory;
    }

    public void setRestaurantCategory(RestaurantCategory restaurantCategory) {
        this.restaurantCategory = restaurantCategory;
    }

    public String getNom_restaurant() {
        return nom_restaurant;
    }

    public void setNom_restaurant(String nom_restaurant) {
        this.nom_restaurant = nom_restaurant;
    }

    public String getAdresse_restaurant() {
        return adresse_restaurant;
    }

    public void setAdresse_restaurant(String adresse_restaurant) {
        this.adresse_restaurant = adresse_restaurant;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "idRestaurant=" + id_restaurant +
                ", restaurantCategory=" + restaurantCategory +
                ", nomRestaurant='" + nom_restaurant + '\'' +
                ", adresseRestaurant='" + adresse_restaurant + '\'' +
                '}';
    }

    public void setImgSrc(String imgSrc) {
    this.imgSrc=imgSrc;}
    public String getImgSrc() {
        return imgSrc;
    }

}

