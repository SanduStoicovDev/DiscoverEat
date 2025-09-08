package hakunamatata.discovereat.data.source.restaurants;

import java.util.List;

import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.RestaurantApiResponse;
import hakunamatata.discovereat.model.SearchResponse;

/**
 * Base class to get restaurants from a local source.
 */
public abstract class BaseRestaurantsLocalDataSource {

    protected RestaurantsCallback restaurantsCallback;

    public void setRestaurantsCallback(RestaurantsCallback restaurantsCallback) {
        this.restaurantsCallback = restaurantsCallback;
    }

    public abstract void getRestaurants(String location);

    public abstract void getFavoriteRestaurants();
    public abstract void updateRestaurant(Restaurant restaurant);
    public abstract void deleteFavoriteRestaurant();
    public abstract void insertRestaurants(RestaurantApiResponse restaurantApiResponse);

    public abstract void insertRestaurant(List<Restaurant> restaurantList);

    //public abstract void insertRestaurant(List<Restaurant> restaurantList);
    public abstract void deleteAll();
}
