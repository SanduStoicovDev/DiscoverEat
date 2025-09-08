package hakunamatata.discovereat.data.source.restaurants;

import java.util.List;

import hakunamatata.discovereat.model.Restaurant;
import hakunamatata.discovereat.model.RestaurantApiResponse;
import hakunamatata.discovereat.model.SearchResponse;

/**
 * Interface to send data from DataSource to Repositories
 * that implement IRestaurantRepositoryWithLiveData interface.
 */
public interface RestaurantsCallback {
    void onSuccessFromRemote(RestaurantApiResponse restaurantApiResponse);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(RestaurantApiResponse restaurantApiResponse);
    void onFailureFromLocal(Exception exception);
    void onRestaurantFavoriteStatusChanged(Restaurant restaurant, List<Restaurant> restaurantList);
    void onRestaurantFavoriteStatusChanged(List<Restaurant> restaurantList);
    void onDeleteFavoriteRestaurantSuccess(List<Restaurant> favoriteRestaurants);
    void onSuccessSynchronization();
    void onSuccessDeletion();
    void onSuccessFromCloudReading(List<Restaurant> restaurantList);
    void onSuccessFromCloudWriting(Restaurant restaurant);
    void onFailureFromCloud(Exception exception);

}
