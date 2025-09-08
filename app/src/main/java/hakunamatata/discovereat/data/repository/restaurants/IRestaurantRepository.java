package hakunamatata.discovereat.data.repository.restaurants;
import hakunamatata.discovereat.model.Restaurant;

/**
 * Interface for Repositories that manage Restaurant objects.
 */
public interface IRestaurantRepository {

    void fetchRestaurants(String location, boolean isRunApi);

    void updateRestaurants(Restaurant restaurant);

    void getFavoriteRestaurant();

    void deleteFavoriteRestaurant();
}