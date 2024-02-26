package entities;

public class Plat {
    private int id_plat;
    private Restaurant restaurant;
    private String nom_plat;
    private double prix;
    private String ingredient;
    private String pimgSrc;

    public Plat() {
    }

    // Updated constructor without id_restaurant
    public Plat(int id_plat, Restaurant restaurant, String nom_plat, double prix, String ingredient, String pimgSrc) {
        this.id_plat = id_plat;
        this.restaurant = restaurant;
        this.nom_plat = nom_plat;
        this.prix = prix;
        this.ingredient = ingredient;
        this.pimgSrc = pimgSrc;
    }

    public String getPimgSrc() {
        return pimgSrc;
    }

    // Corrected setPimgSrc method with parameter
    public void setPimgSrc(String pimgSrc) {
        this.pimgSrc = pimgSrc;
    }

    public int getId_plat() {
        return id_plat;
    }

    public void setId_plat(int id_plat) {
        this.id_plat = id_plat;
    }

    // Updated getter and setter for Restaurant
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getNom_plat() {
        return nom_plat;
    }

    public void setNomPlat(String nomPlat) {
        this.nom_plat = nomPlat;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "Plat{" +
                "id_plat=" + id_plat +
                ", restaurant=" + restaurant +
                ", nom_plat='" + nom_plat + '\'' +
                ", prix=" + prix +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }
}
