package entities;

public class RestaurantCategory {

    private int id;
    private String categoryName;
    private String category_image;  // Add the imagePath attribute

    // Constructors, getters, setters, etc.
    public RestaurantCategory() {}

    public RestaurantCategory(int id, String categoryName, String category_image) {
        this.id = id;
        this.categoryName = categoryName;
        this.category_image = category_image;
    }

    public int getIdCat() {
        return id;
    }
    public void setIdCat(int id){this.id=id;}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
