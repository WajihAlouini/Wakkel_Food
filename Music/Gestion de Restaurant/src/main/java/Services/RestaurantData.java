package Services;

import entities.Restaurant;

import java.util.List;

public class RestaurantData {

    public static List<Restaurant> searchRestaurantsByKeyword(String keyword) {
        // Assuming you have a method in RestaurantService to get search results
        RestaurantService restaurantService = new RestaurantService();
        return restaurantService.getSearchResults(keyword);
    }
}
