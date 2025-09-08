package hakunamatata.discovereat.data.source.restaurants;

import java.util.List;

import hakunamatata.discovereat.model.Restaurant;

/**
 * Base class to get the user favorite Restaurants from a remote source.
 */
public abstract class BaseFavoriteRestaurantsDataSource {
    protected RestaurantsCallback restaurantsCallback;
    public void setRestaurantsCallback(RestaurantsCallback restaurantsCallback) {
        this.restaurantsCallback = restaurantsCallback;
    }

    public abstract void getFavoriteRestaurants();
    public abstract void addFavoriteRestaurant(Restaurant restaurant);
    public abstract void synchronizeFavoriteRestaurants(List<Restaurant> notSynchronizedRestaurantList);
    public abstract void deleteFavoriteRestaurant(Restaurant restaurant);
    public abstract void deleteAllFavoriteRestaurants();
}
