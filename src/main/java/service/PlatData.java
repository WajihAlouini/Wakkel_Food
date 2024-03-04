package Services;
import entities.Plat;
import entities.Restaurant;
import service.PlatService;

import java.util.List;

public class PlatData {
    public static List<Plat> searchPlatsByKeyword(String keyword) {
        // Assuming you have a method in RestaurantService to get search results
        PlatService platService = new PlatService();
        return platService.getSearchResults(keyword);
    }
}
