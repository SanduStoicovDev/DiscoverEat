package hakunamatata.discovereat.util;
import java.util.List;
import hakunamatata.discovereat.model.Restaurant;

/**
 * Interface to send data from Repositories to Activity/Fragment.
 */
public interface ResponseCallback {
    void onSuccess(List<Restaurant> restaurantList, long lastUpdate);
    void onFailure(String errorMessage);
    void onRestaurantsFavoriteStatusChanged(Restaurant restaurant);
}
